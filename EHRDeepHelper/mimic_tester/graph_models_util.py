import torch
import torch.nn as nn
from torch_scatter import scatter_add
from torch_geometric.nn.inits import glorot
import torch.nn.functional as F

class BertLayerNorm(torch.nn.Module):
    def __init__(self, hidden_size, eps=1e-12):
        """Construct a layernorm module in the TF style (epsilon inside the square root).
        """
        super(BertLayerNorm, self).__init__()
        self.weight = nn.Parameter(torch.ones(hidden_size))
        self.bias = nn.Parameter(torch.zeros(hidden_size))
        self.variance_epsilon = eps

    def forward(self, x):
        u = x.mean(-1, keepdim=True)
        s = (x - u).pow(2).mean(-1, keepdim=True)
        x = (x - u) / torch.sqrt(s + self.variance_epsilon)
        return self.weight * x + self.bias

class KNRMRbfKernelBank(torch.nn.Module):
    def __init__(self, mus=None, sigmas=None, dim=0, requires_grad=True):
        super().__init__()
        self.dim = dim
        kernels = [KNRMRbfKernel(m, s, requires_grad=requires_grad) for m, s in zip(mus, sigmas)]
        self.kernels = torch.nn.ModuleList(kernels)

    def count(self):
        return len(self.kernels)

    def forward(self, data):
        #k*num_node1*num_node2
        return torch.stack([k(data) for k in self.kernels], dim=self.dim)


class KNRMRbfKernel(torch.nn.Module):
    def __init__(self, initial_mu, initial_sigma, requires_grad=True):
        super().__init__()
        self.mu = torch.nn.Parameter(torch.tensor(initial_mu), requires_grad=requires_grad)
        self.sigma = torch.nn.Parameter(torch.tensor(initial_sigma), requires_grad=requires_grad)

    def forward(self, data):
        adj = data - self.mu
        return torch.exp(-0.5 * adj * adj / self.sigma / self.sigma)
    
    
class DRMMKernel(torch.nn.Module):
    def __init__(self,bins):
        super().__init__()
        self.bins = bins
        
    def forward(self,sim_data):
        bins = ((sim_data+1.000001)/2.*(self.bins-1)).int()
#         bins_count = torch.bincount(bins.flatten(),minlength=self.bins)
#         return bins_count/torch.sum(bins_count)
        return (torch.bincount(bins.flatten(),minlength=self.bins).float() + 1e-5).log()
#         scores = sim_data.detach()
#         hist = torch.histc(scores,bins = self.bins)
#         hist = hist/torch.sum(hist)
#         return hist
    
    
class NeuralTensorModule(torch.nn.Module):
    def __init__(self,dim,tensors):
        super(NeuralTensorModule, self).__init__()
        self.dim = dim
        self.tensors = tensors
        self.init_parameters()
        
    def init_parameters(self):
        self.weight_matrix = nn.Parameter(torch.Tensor(self.dim,self.dim,self.tensors))
        self.weight_matrix_block = nn.Parameter(torch.Tensor(2*self.dim,self.tensors))
        self.bias = nn.Parameter(torch.zeros(self.tensors))
        
        glorot(self.weight_matrix)
        glorot(self.weight_matrix_block)
        
    def forward(self,emb_1,emb_2):
        #batch*(dim*tensors)->batch*tensors*dim
        scores = torch.mm(emb_1,self.weight_matrix.view(self.dim,-1)).view(-1,self.dim,self.tensors).transpose(1,2)
        #batch*tensors
        matrix_scores = torch.bmm(scores,emb_2.unsqueeze(dim=-1)).squeeze(dim=-1)
        
        mat_blo_scores = torch.mm(torch.cat([emb_1,emb_2],dim=1),self.weight_matrix_block)#batch*tensors
        
        combined_scores = nn.functional.tanh(matrix_scores+mat_blo_scores+self.bias)
        return combined_scores
        
                  
class NodeFilterModule(torch.nn.Module):
    def __init__(self,dim):
        super(NodeFilterModule, self).__init__()
        self.dim = dim
        self.weight_matrix = nn.Parameter(torch.zeros(1,self.dim))
                
        
    def forward(self,emb_1,emb_2):
        #num_nodes1*1*dim num_nodes1*dim*1 ->num_nodes1*1
        weighted_emb_1 = torch.mul(emb_1,self.weight_matrix).unsqueeze(dim=1)
        scores = torch.sigmoid(torch.bmm(weighted_emb_1,emb_2.unsqueeze(dim=1).transpose(1,2)).squeeze(dim=-1))
        
        attented_emb = torch.mul(scores.repeat([1,emb_1.size(1)]), emb_1+emb_2)#
        return attented_emb
    
class MatchFilterModule(torch.nn.Module):
    def __init__(self,dim):
        super(MatchFilterModule, self).__init__()
        self.dim = dim
    
    # for each single graph pair, using the matching signals to filter the valid nodes as the global features
    def batch_singe_pair(self,left_emb,right_emb):#num_node1*dim  num_node2*dim
        matching_matrix = torch.mm(left_emb,right_emb.transpose(0,1)) #num_node1*num_node2
        
        right_atten_emb = torch.mm(nn.functional.softmax(matching_matrix,dim=1),right_emb)#num_node1*dim
        left_atten_emb = torch.mm(torch.transpose(nn.functional.softmax(matching_matrix,dim=0),0,1),left_emb)#num_node2*dim
        
        #num_node1*1*dim num_node1*dim*1 ->num_node1*1
        match_left_weight = torch.sigmoid(torch.matmul(left_emb.unsqueeze(dim=1),right_atten_emb.unsqueeze(dim=-1)).squeeze(dim=-1))
        matched_left_emb = torch.mul(match_left_weight.repeat([1,left_emb.size(1)]), left_emb)
        match_right_weight = torch.sigmoid(torch.matmul(right_emb.unsqueeze(dim=1),left_atten_emb.unsqueeze(dim=-1)).squeeze(dim=-1))
        matched_right_emb = torch.mul(match_right_weight.repeat([1,right_emb.size(1)]), right_emb)
        
        return matched_left_emb, matched_right_emb
        
        
        
    def forward(self,left_graph_emb,right_graph_emb,left_x_batch,right_x_batch):
        left_num_nodes = scatter_add(left_x_batch.new_ones(left_graph_emb.size(0)),left_x_batch,dim=0)
        right_num_nodes = scatter_add(right_x_batch.new_ones(right_graph_emb.size(0)),right_x_batch,dim=0)
                
        
        left_start_idx = torch.cat([left_num_nodes.new_zeros(1),left_num_nodes.cumsum(dim=0)[:-1]],dim=0)
        right_start_idx = torch.cat([right_num_nodes.new_zeros(1),right_num_nodes.cumsum(dim=0)[:-1]],dim=0)
        
        left_batch_nodes = []
        for start_,len_ in zip(left_start_idx,left_num_nodes):
            left_batch_nodes.append(left_graph_emb[start_:start_+len_,:])
        right_batch_nodes = []
        for start_,len_ in zip(right_start_idx,right_num_nodes):
            right_batch_nodes.append(right_graph_emb[start_:start_+len_,:])
        
        batch_graph_global_emb = [[],[]]
        for left_emb, right_emb in zip(left_batch_nodes,right_batch_nodes):
            matched_left_emb, matched_right_emb  = self.batch_singe_pair(left_emb, right_emb)
            batch_graph_global_emb[0].append(torch.sum(matched_left_emb,dim=0))
            batch_graph_global_emb[1].append(torch.sum(matched_right_emb,dim=0))
            
        left_global_emb = torch.stack(batch_graph_global_emb[0],dim=0)#batch*dim
        right_global_emb = torch.stack(batch_graph_global_emb[1],dim=0)
        
        return left_global_emb, right_global_emb
    
class MatchFilterModule2(torch.nn.Module):
    def __init__(self,dim):
        super(MatchFilterModule2, self).__init__()
        self.dim = dim
        
        self.weight_matrix = nn.Parameter(torch.Tensor(self.dim,self.dim))
        glorot(self.weight_matrix)
    
    # for each single graph pair, using the max matching signal and then apply the softmax operator to get the final global graph embedding
    def batch_singe_pair(self,left_emb,right_emb):#dim  dim
        matching_matrix = F.dropout(F.tanh(torch.mm(torch.mm(left_emb,self.weight_matrix),torch.transpose(right_emb,0,1)))) #num_node1*num_node2
        row_max_mat, _ = torch.max(matching_matrix,dim=1)#num_node1
        col_max_mat, _ = torch.max(matching_matrix,dim=0)#num_node2
        row_wise_weight, col_wise_weight = torch.unsqueeze(F.softmax(row_max_mat),dim=-1), torch.unsqueeze(F.softmax(col_max_mat),dim=-1)  #num_node1*1 num_node2*1
        
        match_left_emb = torch.squeeze(torch.mm(torch.transpose(left_emb,0,1),row_wise_weight),dim=-1)
        match_right_emb = torch.squeeze(torch.mm(torch.transpose(right_emb,0,1),col_wise_weight),dim=-1)
        
        return match_left_emb, match_right_emb
        """matching_matrix = torch.mm(left_emb,right_emb.transpose(0,1)) #num_node1*num_node2
        
        right_atten_emb = torch.mm(nn.functional.softmax(matching_matrix,dim=1),right_emb)#num_node1*dim
        left_atten_emb = torch.mm(torch.transpose(nn.functional.softmax(matching_matrix,dim=0),0,1),left_emb)#num_node2*dim
        
        #num_node1*1*dim num_node1*dim*1 ->num_node1*1
        match_left_weight = torch.sigmoid(torch.matmul(left_emb.unsqueeze(dim=1),right_atten_emb.unsqueeze(dim=-1)).squeeze(dim=-1))
        matched_left_emb = torch.mul(match_left_weight.repeat([1,left_emb.size(1)]), left_emb)
        match_right_weight = torch.sigmoid(torch.matmul(right_emb.unsqueeze(dim=1),left_atten_emb.unsqueeze(dim=-1)).squeeze(dim=-1))
        matched_right_emb = torch.mul(match_right_weight.repeat([1,right_emb.size(1)]), right_emb)
        
        return matched_left_emb, matched_right_emb"""
        
        
        
    def forward(self,left_graph_emb,right_graph_emb,left_x_batch,right_x_batch):
        left_num_nodes = scatter_add(left_x_batch.new_ones(left_graph_emb.size(0)),left_x_batch,dim=0)
        right_num_nodes = scatter_add(right_x_batch.new_ones(right_graph_emb.size(0)),right_x_batch,dim=0)
                
        
        left_start_idx = torch.cat([left_num_nodes.new_zeros(1),left_num_nodes.cumsum(dim=0)[:-1]],dim=0)
        right_start_idx = torch.cat([right_num_nodes.new_zeros(1),right_num_nodes.cumsum(dim=0)[:-1]],dim=0)
        
        left_batch_nodes = []
        for start_,len_ in zip(left_start_idx,left_num_nodes):
            left_batch_nodes.append(left_graph_emb[start_:start_+len_,:])
        right_batch_nodes = []
        for start_,len_ in zip(right_start_idx,right_num_nodes):
            right_batch_nodes.append(right_graph_emb[start_:start_+len_,:])
        
        batch_graph_global_emb = [[],[]]
        for left_emb, right_emb in zip(left_batch_nodes,right_batch_nodes):
            matched_left_emb, matched_right_emb  = self.batch_singe_pair(left_emb, right_emb)
            batch_graph_global_emb[0].append(matched_left_emb)
            batch_graph_global_emb[1].append(matched_right_emb)
            
        left_global_emb = torch.stack(batch_graph_global_emb[0],dim=0)#batch*dim
        right_global_emb = torch.stack(batch_graph_global_emb[1],dim=0)
        
        return left_global_emb, right_global_emb
        
        

    
