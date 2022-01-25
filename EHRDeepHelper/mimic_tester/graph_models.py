from __future__ import absolute_import,division,print_function
import torch
import torch.nn as nn
import torch.nn.functional as F
from torch.nn import LayerNorm
from torch.nn import CosineSimilarity 
import numpy as np
# import transformers
# from transformers import BertModel

import inspect
from torch_scatter import scatter_add
from torch_geometric.utils import softmax, add_self_loops
from torch_geometric.nn.inits import glorot, zeros, uniform
from torch_geometric.nn import GCNConv, GATConv,SAGPooling,ASAPooling,TransformerConv,GINConv,GatedGraphConv
from torch_geometric.nn import global_add_pool as add_p, global_max_pool as max_p, global_mean_pool as mean_p

from build_tree import build_stage_one_edges, build_stage_two_edges, build_cominbed_edges,  build_ancestors
from build_tree import build_diag_tree, build_atc_tree, build_proce_tree

from graph_models_util import KNRMRbfKernelBank,DRMMKernel,NeuralTensorModule,NodeFilterModule,MatchFilterModule,MatchFilterModule2


def gelu(x):
    return 0.5 * x * (1 + torch.tanh(math.sqrt(2 / math.pi) * (x + 0.044715 * torch.pow(x, 3))))
  

def norm_sim(sim_val):
    return 1-torch.arccos(sim_val)/np.pi
    
class ContrastiveLoss(nn.Module):
    """
    Contrastive loss
    Takes embeddings of two samples and a target label == 1 if samples are from the same class and label == 0 otherwise
    """

    def __init__(self, margin=1.0):
        super(ContrastiveLoss, self).__init__()
        self.margin = margin

    def forward(self, outputs, labels, size_average=True):
        
        losses = 0.5 * ((1-labels) * torch.pow(outputs,2) +
                        labels * torch.pow(torch.clamp(self.margin-outputs,min=0),2) )
        return losses.mean() if size_average else losses.sum()

class TestModel(nn.Module):
    def __init__(self, config):#,in_channels=100,out_channels=100
        super(TestModel,self).__init__()
        
        self.all_embedding = nn.Embedding(config.vocab_size,config.emb_size)
        
#         self.classifier = nn.Linear(config.out_channels*2,1)
        self.trans_linear = nn.Linear(config.emb_size,config.emb_size)
        
        
    def forward(self,left_x,left_graph_index,right_x,right_graph_index,left_x_batch,right_x_batch,label=None):

        left_graph_emb = self.all_embedding(left_x).squeeze(dim=1)
        right_graph_emb = self.all_embedding(right_x).squeeze(dim=1)

#         left_graph_emb, left_graph_index, _ , left_x_batch , _ , _  = self.gcn_pooling(left_graph_emb,left_graph_index,batch=left_x_batch)
#         left_graph_final_emb = mean_p(left_graph_emb,left_x_batch) #batch*graph_hidden_size for graph vector
        left_graph_final_emb = self.trans_linear(max_p(left_graph_emb,left_x_batch).squeeze(dim=1))

    
        
#         right_graph_emb, right_graph_index, _ , right_x_batch , _ , _ = self.gcn_pooling(right_graph_emb,right_graph_index,batch=right_x_batch)
#         right_graph_final_emb = mean_p(right_graph_emb,right_x_batch) #batch*graph_hidden_size  
        right_graph_final_emb = self.trans_linear(max_p(right_graph_emb,right_x_batch).squeeze(dim=1)) #batch*graph_hidden_size

#         outputs = self.classifier(torch.cat([left_graph_final_emb,right_graph_final_emb],dim=1))
        cos_sim = CosineSimilarity(eps=1e-6)
        outputs =  cos_sim(left_graph_final_emb, right_graph_final_emb)  # squared distances
        
        if label is not None:
            loss_fct = ContrastiveLoss()
            sim_loss = loss_fct(outputs.view(-1), label.view(-1))
            outputs = [sim_loss, outputs]
        
        return outputs

class EHRModel(nn.Module):
    def __init__(self, config):#,in_channels=100,out_channels=100
        super(EHRModel,self).__init__()
        
        
#         self.atc_embedding = OntologyEmbedding(atc_voc, build_atc_tree,
#                                               config.hidden_size, config.graph_hidden_size,
#                                               config.graph_heads)
#         self.diag_embedding = OntologyEmbedding(diag_voc, build_diag_tree,
#                                               config.hidden_size, config.graph_hidden_size,
#                                               config.graph_heads)
#         self.proce_embedding = OntologyEmbedding(proce_voc, build_proce_tree,
#                                               config.hidden_size, config.graph_hidden_size,
#                                               config.graph_heads)
        
#         self.all_embedding = torch.cat(
#             [self.diag_embedding(),self.proce_embedding(),self.atc_embedding()], dim=0)
#         self.all_embedding = self.all_embedding.to(device)
        self.all_embedding = nn.Embedding(config.vocab_size,config.emb_size)
        self.gcn_conv_list = nn.ModuleList()
        for _ in range(config.gcn_conv_nums-1):
            self.gcn_conv_list.append(GCNConv(in_channels=config.graph_hidden_size,out_channels=config.graph_hidden_size))
        self.gcn_conv_list.append(GCNConv(in_channels=config.graph_hidden_size,out_channels=config.out_channels)) 
#         self.gcn_pooling = SAGPooling(in_channels=config.out_channels)
        
#         self.classifier = nn.Linear(config.out_channels*2,2)
        
    def get_final_node(self,x,batch):
        num_nodes = scatter_add(batch.new_ones(x.size(0)),batch,dim=0)
        cum_num_nodes = torch.cat([num_nodes.new_zeros(1),num_nodes.cumsum(dim=0)[:-1]],dim=0)
        return x[cum_num_nodes,:]

        
    def forward(self,left_x,left_graph_index,right_x,right_graph_index,left_x_batch,right_x_batch,label=None):
#         batch_all_embedding = self.all_embedding.unsqueeze(0).repeat(left_graph.size()[0],1,1)
#         print(batch_all_embedding.shape)
        
#         left_graph = left_graph.squeeze(dim=0)
#         right_graph = right_graph.squeeze(dim=0)
#         label = label.squeeze(dim=0)
        left_x = self.all_embedding(left_x).squeeze(dim=1)
        right_x = self.all_embedding(right_x).squeeze(dim=1)
        
        for gcn_conv in self.gcn_conv_list[:-1]:
            left_x = gcn_conv(left_x,left_graph_index)
            right_x = gcn_conv(right_x,right_graph_index)
        
        left_graph_emb = self.gcn_conv_list[-1](left_x,left_graph_index)
        right_graph_emb = self.gcn_conv_list[-1](right_x,right_graph_index)     
        
        left_graph_final_emb = self.get_final_node(left_graph_emb,left_x_batch)
        
        right_graph_final_emb = self.get_final_node(right_graph_emb,right_x_batch)
#         left_graph_emb, left_graph_index, _ , left_x_batch , _ , _  = self.gcn_pooling(left_graph_emb,left_graph_index,batch=left_x_batch)
#         left_graph_final_emb = mean_p(left_graph_emb,left_x_batch) #batch*graph_hidden_size for graph vector
#         left_graph_final_emb = max_p(left_graph_emb,left_x_batch)
    
        
#         right_graph_emb, right_graph_index, _ , right_x_batch , _ , _ = self.gcn_pooling(right_graph_emb,right_graph_index,batch=right_x_batch)
#         right_graph_final_emb = mean_p(right_graph_emb,right_x_batch) #batch*graph_hidden_size  
#         right_graph_final_emb = max_p(right_graph_emb,right_x_batch) #batch*graph_hidden_size
        
#         outputs = self.classifier(torch.cat([left_graph_final_emb,right_graph_final_emb],dim=1))
        
#         if label is not None:
#             loss_fct = nn.CrossEntropyLoss(ignore_index=-1)
#             sim_loss = loss_fct(outputs.view(-1, 2), label.view(-1))
#             outputs = [sim_loss, outputs]

        cos_sim = CosineSimilarity(eps=1e-6)
        outputs =  cos_sim(left_graph_final_emb, right_graph_final_emb)  # squared distances
        
        if label is not None:
            loss_fct = ContrastiveLoss()
            sim_loss = loss_fct(outputs.view(-1), label.view(-1))
            outputs = [sim_loss, outputs]
        
        return outputs
    
class GRAMModel(nn.Module):
    def __init__(self, config,vocab_emb,diag_voc,proce_voc,atc_voc,device):#,in_channels=100,out_channels=100
        super(GRAMModel,self).__init__()
        self.atc_embedding = DAGEmbedding(atc_voc, build_atc_tree,
                                              config.hidden_size, config.graph_hidden_size,
                                              config.graph_heads,level=5)
        self.diag_embedding = DAGEmbedding(diag_voc, build_diag_tree,
                                              config.hidden_size, config.graph_hidden_size,
                                              config.graph_heads,level=4)
        self.proce_embedding = DAGEmbedding(proce_voc, build_proce_tree,
                                              config.hidden_size, config.graph_hidden_size,
                                              config.graph_heads,level=4)
        self.neural_sim_layer_1 = NeuralTensorModule(config.graph_hidden_size,config.pair_neurons)
        self.fully_connected_layer = nn.Linear(config.pair_neurons,1)

    def forward(self,left_x,left_graph_index,right_x,right_graph_index,left_x_batch,right_x_batch,left_diag_cnt,right_diag_cnt,label=None):
        all_embedding = torch.cat([self.diag_embedding(),self.proce_embedding(),self.atc_embedding()], dim=0)
        left_onto_x = all_embedding[left_x].squeeze(dim=1)#batch*len*dim
        right_onto_x = all_embedding[right_x].squeeze(dim=1)
        
        
        left_self_global_emb, right_self_global_emb = add_p(left_onto_x,left_x_batch).squeeze(dim=1), add_p(right_onto_x,right_x_batch).squeeze(dim=1)
        
        
        pair_sim = self.neural_sim_layer_1(left_self_global_emb,right_self_global_emb)
        outputs =  torch.sigmoid(self.fully_connected_layer(pair_sim))
        
        
        outputs = outputs.squeeze(dim=1)
        if label is not None:
            loss_fct = nn.MSELoss()
            sim_loss = loss_fct(outputs.view(-1), label.view(-1))
            outputs = [sim_loss, outputs]
        
        return outputs

    
class BaseEHROntologyModel(nn.Module):
    def __init__(self, config,vocab_emb,image_vocab_emb,diag_voc,proce_voc,atc_voc,device):#,in_channels=100,out_channels=100
        super(BaseEHROntologyModel,self).__init__()
        
        self.atc_embedding = OntologyEmbedding(atc_voc, build_atc_tree,
                                              config.hidden_size, config.graph_hidden_size,
                                              config.graph_heads)
        self.diag_embedding = OntologyEmbedding(diag_voc, build_diag_tree,
                                              config.hidden_size, config.graph_hidden_size,
                                              config.graph_heads)
        self.proce_embedding = OntologyEmbedding(proce_voc, build_proce_tree,
                                              config.hidden_size, config.graph_hidden_size,
                                              config.graph_heads)

        self.desc_embedding = nn.Parameter(vocab_emb)
        glorot(self.desc_embedding)
        
        self.image_embedding = nn.Parameter(image_vocab_emb)
        glorot(self.image_embedding)
        
        
        self.desc_linear = nn.Linear(config.desc_emb_size,config.hidden_size)
        self.image_linear = nn.Linear(config.img_emb_size,config.hidden_size)
        
        if config.use_emb == 2:#2 for three embeddings  1 for multimodal embeddings  0 for ontology embeddings        
            self.concat_emb_size = 3*config.hidden_size            
        elif config.use_emb == 1 or config.use_emb == 5 or config.use_emb == 6:
            self.concat_emb_size = 2*config.hidden_size
        else:
            self.concat_emb_size = config.hidden_size
            
        self.use_emb = config.use_emb
        self.out_channels = self.concat_emb_size
        self.gcn_conv_list = nn.ModuleList()
        if config.use_conv == 'gin':
            self.gin_mlp = nn.Sequential(nn.Linear(self.concat_emb_size,self.concat_emb_size),nn.ReLU(),nn.Linear(self.concat_emb_size,self.concat_emb_size))
            for _ in range(config.gcn_conv_nums-1):
                self.gcn_conv_list.append(GINConv(self.gin_mlp))
            self.gcn_conv_list.append(GINConv(nn.Sequential(nn.Linear(self.concat_emb_size,self.concat_emb_size),nn.ReLU(),nn.Linear(self.concat_emb_size,self.out_channels))))
        elif config.use_conv == 'gat':
            for _ in range(config.gcn_conv_nums-1):
                self.gcn_conv_list.append(GATConv(in_channels = self.concat_emb_size, out_channels = self.concat_emb_size//config.num_heads , heads=config.num_heads))
            self.gcn_conv_list.append(GATConv(in_channels = self.concat_emb_size, out_channels = self.out_channels//config.num_heads , heads=config.num_heads))
        elif config.use_conv == 'gcn':
            for _ in range(config.gcn_conv_nums-1):
                self.gcn_conv_list.append(GCNConv(in_channels=self.concat_emb_size,out_channels=self.concat_emb_size))
            self.gcn_conv_list.append(GCNConv(in_channels=self.concat_emb_size,out_channels=self.out_channels))
        elif config.use_conv == 'gated_conv':
            for _ in range(config.gcn_conv_nums-1):
                self.gcn_conv_list.append(GatedGraphConv(out_channels=self.concat_emb_size, num_layers = 1))
            self.gcn_conv_list.append(GatedGraphConv(out_channels=self.out_channels, num_layers = 1))
#         for _ in range(config.gcn_conv_nums-1):
#             self.gcn_conv_list.append(GCNConv(in_channels=self.concat_emb_size,out_channels=self.concat_emb_size))
#         self.gcn_conv_list.append(GCNConv(in_channels=self.concat_emb_size,out_channels=config.out_channels))
        
        self.bn1 = torch.nn.BatchNorm1d(self.concat_emb_size)
    
        self.global_tensor_dim = (1+config.gcn_conv_nums+1)*self.out_channels if config.use_cross_atten else self.out_channels
        self.neural_sim_layer_1 = NeuralTensorModule(self.global_tensor_dim,config.pair_neurons)

        self.nodes_filter_layer = NodeFilterModule(self.out_channels)
        if config.use_attn == 1:
            self.nodes_atten_layer = MatchFilterModule(self.out_channels)
        elif config.use_attn == 2:
            self.nodes_atten_layer = MatchFilterModule2(self.out_channels)
        
        self.use_glb = config.use_glb #0 for global info, 1 for local info, and 2 for both info
        self.use_cross_atten = config.use_cross_atten

        

    def repeat_global_emb(self,global_emb,batch,nodes_emb):
        num_nodes = scatter_add(batch.new_ones(nodes_emb.size(0)),batch,dim=0).cpu().numpy()
        global_emb_split = torch.split(global_emb,1,dim=0) # split the global embedding along the batch_size axis
        assert len(global_emb_split)==len(num_nodes)
        
        global_emb_ls = []
        for i in range(len(global_emb_split)):
            global_emb_ls.append(global_emb_split[i].repeat(num_nodes[i],1))
        global_emb_mat = torch.cat(global_emb_ls,dim=0)#num_nodes*size
        return global_emb_mat                
    
    
    # fuse the attention mechanism for left and right graph
    def get_attention_emb(self,left_graph_emb,right_graph_emb,left_x_batch,right_x_batch,left_diag_cnt,right_diag_cnt):
        raise NotImplementedError
        
    def encode(self,left_x,left_graph_index,right_x,right_graph_index,left_x_batch,right_x_batch,left_diag_cnt,right_diag_cnt):
        all_embedding = torch.cat(
            [self.diag_embedding(),self.proce_embedding(),self.atc_embedding()], dim=0)
        
        left_onto_x = all_embedding[left_x].squeeze(dim=1)
        right_onto_x = all_embedding[right_x].squeeze(dim=1)
        
        left_desc_x = self.desc_linear(self.desc_embedding[left_x].squeeze(dim=1))
        right_desc_x = self.desc_linear(self.desc_embedding[right_x].squeeze(dim=1))
        
        left_img_x = self.image_linear(self.image_embedding[left_x].squeeze(dim=1))
        right_img_x = self.image_linear(self.image_embedding[right_x].squeeze(dim=1))
        
        if self.use_emb == 2:        
            left_x = torch.cat([left_onto_x,left_desc_x,left_img_x],dim=-1)
            right_x = torch.cat([right_onto_x,right_desc_x,right_img_x],dim=-1)
        elif self.use_emb == 1:
            left_x = torch.cat([left_desc_x,left_img_x],dim=-1)
            right_x = torch.cat([right_desc_x,right_img_x],dim=-1)
        elif self.use_emb == 0:
            left_x, right_x = left_onto_x, right_onto_x
        elif self.use_emb == 3:
            left_x, right_x =  left_img_x, right_img_x
        elif self.use_emb == 4:
            left_x, right_x = left_desc_x, right_desc_x
        elif self.use_emb == 5:
            left_x = torch.cat([left_onto_x,left_desc_x], dim = -1)
            right_x = torch.cat([right_onto_x,right_desc_x], dim = -1)
        elif self.use_emb == 6:
            left_x = torch.cat([left_onto_x,left_img_x], dim = -1)
            right_x = torch.cat([right_onto_x,right_img_x], dim = -1)
            
        
        left_n_hops, right_n_hops = [left_x], [right_x] #for each hop structure, do the cross graph matching

        graph_match = [self.get_attention_emb(left_x,right_x,left_x_batch,right_x_batch,left_diag_cnt,right_diag_cnt)]
        for gcn_conv in self.gcn_conv_list[:-1]:
            left_x = F.dropout(F.elu(gcn_conv(left_x,left_graph_index)))
            right_x = F.dropout(F.elu(gcn_conv(right_x,right_graph_index)))
            
            left_n_hops.append(left_x)#
            right_n_hops.append(right_x)#
            
            graph_match.append(self.get_attention_emb(left_x,right_x,left_x_batch,right_x_batch,left_diag_cnt,right_diag_cnt))
        
        left_graph_emb = self.gcn_conv_list[-1](left_x,left_graph_index)
        right_graph_emb = self.gcn_conv_list[-1](right_x,right_graph_index)
        
        left_n_hops.append(left_graph_emb)#
        right_n_hops.append(right_graph_emb)#
        
        graph_match.append(self.get_attention_emb(left_graph_emb,right_graph_emb,left_x_batch,right_x_batch,left_diag_cnt,right_diag_cnt))#batch*bins 
        
        graph_drmms_score = torch.cat(graph_match,dim=-1)
        
        
        left_self_emb = self.nodes_filter_layer(left_graph_emb,left_x)
        right_self_emb = self.nodes_filter_layer(right_graph_emb,right_x)
        
        
        
        left_self_global_emb, right_self_global_emb = add_p(left_self_emb,left_x_batch).squeeze(dim=1), add_p(right_self_emb,right_x_batch).squeeze(dim=1)
        
        if self.use_cross_atten:
            left_self_atten_global_emb, right_self_atten_global_emb = [left_self_global_emb], [right_self_global_emb]#

            for left_i_hop, right_i_hop in zip(left_n_hops,right_n_hops):
                left_atten_global_emb, right_atten_global_emb = self.nodes_atten_layer(left_i_hop,right_i_hop,left_x_batch,right_x_batch)
                left_self_atten_global_emb.append(left_atten_global_emb)
                right_self_atten_global_emb.append(right_atten_global_emb)
        
        
            left_self_global_emb, right_self_global_emb = torch.cat(left_self_atten_global_emb,dim=-1), torch.cat(right_self_atten_global_emb,dim=-1)#
        
        
        pair_sim = self.neural_sim_layer_1(left_self_global_emb,right_self_global_emb)
        
        return pair_sim, graph_drmms_score
     
    
    def forward(self,left_x,left_graph_index,right_x,right_graph_index,left_x_batch,right_x_batch,left_diag_cnt,right_diag_cnt,label=None):
        raise NotImplementedError
    
    
class KNRMEHROntologyModel(BaseEHROntologyModel):
    def __init__(self, config,vocab_emb,image_vocab_emb,diag_voc,proce_voc,atc_voc,device):#,in_channels=100,out_channels=100
        super(KNRMEHROntologyModel,self).__init__(config,vocab_emb,image_vocab_emb,diag_voc,proce_voc,atc_voc,device)       
        
        MUS = [-0.9, -0.7, -0.5, -0.3, -0.1, 0.1, 0.3, 0.5, 0.7, 0.9, 1.0]
        SIGMAS = [0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.001]
        
        self.kernels = KNRMRbfKernelBank(MUS,SIGMAS)
        
        self.kernel_linear = nn.Linear(self.kernels.count(),1)
        
        self.use_glb = config.use_glb #0 for global info, 1 for local info, and 2 for both info
        
        self.attn_cnt = config.gcn_conv_nums+1
        
        if self.use_glb==0:self.fully_connected_layer = nn.Linear(config.pair_neurons,1)
        elif self.use_glb==1:self.fully_connected_layer = nn.Linear(self.kernels.count()*self.attn_cnt,1)#config.bins
        else:self.fully_connected_layer = nn.Linear(config.pair_neurons+self.kernels.count()*self.attn_cnt,1)            
    
    
    # fuse the attention mechanism for left and right graph
    # fuse the attention mechanism for left and right graph
    def get_attention_emb(self,left_graph_emb,right_graph_emb,left_x_batch,right_x_batch,left_diag_cnt,right_diag_cnt):
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
        
        kernels_res,simmat_ = [],[]
        for left_nodes,right_nodes,left_diags,right_diags in zip(left_batch_nodes,right_batch_nodes,left_diag_cnt,right_diag_cnt):#num_node1*dim, num_node2*dim
            
            left_nodes, right_nodes = left_nodes[:left_diags.cpu(),:], right_nodes[:right_diags.cpu(),:]             
            
            left_norm = left_nodes.norm(p=2,dim=1).reshape(left_nodes.size(0),1).repeat(1,right_nodes.size(0)) + 1e-9 #num_node1*num_node2
            right_norm = right_nodes.norm(p=2,dim=1).reshape(1,right_nodes.size(0)).repeat(left_nodes.size(0),1) + 1e-9
            

            simmat = left_nodes.mm(right_nodes.transpose(0,1))#node1*node2
            simmat = simmat/(left_norm*right_norm)
            
            kernels = self.kernels(simmat)#kernel_num*num_node1*num_node2
            simmat_kernel = (kernels.sum(dim=2) + 1e-6).log().sum(dim=1)#k
            kernels_res.append(simmat_kernel)
        return torch.stack(kernels_res,dim=0)#batch*k
     
    
    def forward(self,left_x,left_graph_index,right_x,right_graph_index,left_x_batch,right_x_batch,left_diag_cnt,right_diag_cnt,label=None):
       
        pair_sim, knrm_score = self.encode(left_x,left_graph_index,right_x,right_graph_index,left_x_batch,right_x_batch,left_diag_cnt,right_diag_cnt)  
#         print(knrm_score)
        
        if self.use_glb==0:outputs =  torch.sigmoid(self.fully_connected_layer(pair_sim)) # global infos
        elif self.use_glb==1:outputs =  torch.sigmoid(self.fully_connected_layer(knrm_score)) # local infos
        elif self.use_glb==2:outputs = torch.sigmoid(self.fully_connected_layer(torch.cat([pair_sim,knrm_score],dim=1)))
#             outputs = self.gamma*torch.sigmoid(self.fully_connected_layer_1(pair_sim)) + (1-self.gamma)*torch.sigmoid(self.fully_connected_layer_2(graph_drmms_score))# squared distances both info                                                             
                                                                                                                      
        outputs = outputs.squeeze(dim=1)
#         print(outputs)
        
        if label is not None:
#             loss_fct = ContrastiveLoss()
            loss_fct = nn.MSELoss()
            sim_loss = loss_fct(outputs.view(-1), label.view(-1))
            outputs = [sim_loss, outputs]
        
        return outputs
    
class DRMMEHROntologyModel(BaseEHROntologyModel):
    def __init__(self, config,vocab_emb,image_vocab_emb,diag_voc,proce_voc,atc_voc,device):#,in_channels=100,out_channels=100
        super(DRMMEHROntologyModel,self).__init__(config,vocab_emb,image_vocab_emb,diag_voc,proce_voc,atc_voc,device)
        
        self.bins = config.bins
        self.drmm_kernel = DRMMKernel(self.bins)
        self.drmm_linear = nn.Linear(self.bins,self.bins)
#         self.global_linear = nn.Linear(self.neurons,1)
#         self.local_linear = nn.Linear(self.bins,1)#(config.gcn_conv_nums+1)*
        """self.transformer_onto_list = nn.ModuleList()
        self.transformer_desc_list = nn.ModuleList()
        for _ in range(config.self_attn_nums):
            self.transformer_onto_list.append(TransformerConv(in_channels=config.hidden_size,out_channels=config.hidden_size//4,heads=4))##GTransformerBlock(config)
            self.transformer_desc_list.append(TransformerConv(in_channels=config.hidden_size,out_channels=config.hidden_size//4,heads=4))"""
        
        """self.gcn_conv_list = nn.ModuleList()
        """
        
        """self.gcn_conv_list = nn.ModuleList()
        for _ in range(config.gcn_conv_nums-1):
            self.gcn_conv_list.append(TransformerConv(in_channels=self.concat_emb_size,out_channels=self.concat_emb_size//config.num_heads,heads=config.num_heads))
        self.gcn_conv_list.append(TransformerConv(in_channels=self.concat_emb_size,out_channels=self.concat_emb_size//config.num_heads,heads=config.num_heads))"""
        
#         self.gcn_conv_list = nn.ModuleList()
#         if config.use_conv == 'gin':
#             self.gin_mlp = nn.Sequential(nn.Linear(self.concat_emb_size,self.concat_emb_size),nn.ReLU(),nn.Linear(self.concat_emb_size,self.concat_emb_size))
#             for _ in range(config.gcn_conv_nums-1):
#                 self.gcn_conv_list.append(GINConv(self.gin_mlp))
#             self.gcn_conv_list.append(GINConv(self.gin_mlp))
#         elif config.use_conv == 'gat':
#             for _ in range(config.gcn_conv_nums-1):
#                 self.gcn_conv_list.append(GATConv(in_channels = self.concat_emb_size, out_channels = self.concat_emb_size//config.num_heads , heads=config.num_heads))
#             self.gcn_conv_list.append(GATConv(in_channels = self.concat_emb_size, out_channels = self.concat_emb_size//config.num_heads , heads=config.num_heads))
#         elif config.use_conv == 'gcn':
#             for _ in range(config.gcn_conv_nums-1):
#                 self.gcn_conv_list.append(GCNConv(in_channels=self.concat_emb_size,out_channels=self.concat_emb_size))
#             self.gcn_conv_list.append(GCNConv(in_channels=self.concat_emb_size,out_channels=self.concat_emb_size))
#         elif config.use_conv == 'gated_conv':
#             for _ in range(config.gcn_conv_nums-1):
#                 self.gcn_conv_list.append(GatedGraphConv(out_channels=self.concat_emb_size, num_layers = 1))
#             self.gcn_conv_list.append(GatedGraphConv(out_channels=self.concat_emb_size, num_layers = 1))
#         self.bn1 = torch.nn.BatchNorm1d(self.concat_emb_size)
        
        self.attn_cnt = config.gcn_conv_nums+1
        self.use_glb = config.use_glb #0 for global info, 1 for local info, and 2 for both info
        if self.use_glb==0:self.fully_connected_layer = nn.Linear(config.pair_neurons,1)
        elif self.use_glb==1:self.fully_connected_layer = nn.Linear(config.bins*self.attn_cnt,1)#config.bins
        else:self.fully_connected_layer = nn.Linear(config.pair_neurons+config.bins*self.attn_cnt,1)
        
#         self.trans_linear = nn.Linear(config.out_channels,config.out_channels)
        
#         self.gcn_conv = GCNConv(in_channels=config.graph_hidden_size,out_channels=config.out_channels)            
    
    
    # fuse the attention mechanism for left and right graph
    def get_attention_emb(self,left_graph_emb,right_graph_emb,left_x_batch,right_x_batch,left_diag_cnt,right_diag_cnt):
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
        
        kernels_res,simmat_ = [],[]
        for left_nodes,right_nodes,left_diags,right_diags in zip(left_batch_nodes,right_batch_nodes,left_diag_cnt,right_diag_cnt):#num_node1*dim, num_node2*dim
            
            left_nodes, right_nodes = left_nodes[:left_diags.cpu(),:], right_nodes[:right_diags.cpu(),:]             
            
            left_norm = left_nodes.norm(p=2,dim=1).reshape(left_nodes.size(0),1).repeat(1,right_nodes.size(0)) + 1e-9 #num_node1*num_node2
            right_norm = right_nodes.norm(p=2,dim=1).reshape(1,right_nodes.size(0)).repeat(left_nodes.size(0),1) + 1e-9
            

            simmat = left_nodes.mm(right_nodes.transpose(0,1))#node1*node2
            simmat = simmat/(left_norm*right_norm)
            
            simmat_.append(simmat)
            
            kernels_res.append(self.drmm_kernel(simmat))
  
        match_drmm_signal = torch.stack(kernels_res,dim=0)#batch*bins
        return match_drmm_signal
    
#     def encode(self,left_x,left_graph_index,right_x,right_graph_index,left_x_batch,right_x_batch,left_diag_cnt,right_diag_cnt):
#         all_embedding = torch.cat(
#             [self.diag_embedding(),self.proce_embedding(),self.atc_embedding()], dim=0)
        
#         left_onto_x = all_embedding[left_x].squeeze(dim=1)
#         right_onto_x = all_embedding[right_x].squeeze(dim=1)
        
# #         for transformer in self.transformer_onto_list:
# #             left_onto_x = transformer(left_onto_x,left_graph_index)
# #             right_onto_x = transformer(right_onto_x,right_graph_index)
        
#         left_desc_x = self.desc_linear(self.desc_embedding[left_x].squeeze(dim=1))
#         right_desc_x = self.desc_linear(self.desc_embedding[right_x].squeeze(dim=1))
        
# #         for transformer in self.transformer_desc_list:
# #             left_desc_x = transformer(left_desc_x,left_graph_index)
# #             right_desc_x = transformer(right_desc_x,right_graph_index)
        
#         left_x = torch.cat([left_onto_x,left_desc_x],dim=-1)
#         right_x = torch.cat([right_onto_x,right_desc_x],dim=-1)

        
#         graph_match = [self.get_attention_emb(left_x,right_x,left_x_batch,right_x_batch,left_diag_cnt,right_diag_cnt)]
#         for gcn_conv in self.gcn_conv_list[:-1]:
#             left_x = self.bn1(F.relu(gcn_conv(left_x,left_graph_index)))
#             right_x = self.bn1(F.relu(gcn_conv(right_x,right_graph_index))) 
            
#             graph_match.append(self.get_attention_emb(left_x,right_x,left_x_batch,right_x_batch,left_diag_cnt,right_diag_cnt))
        
#         left_graph_emb = self.gcn_conv_list[-1](left_x,left_graph_index)
#         right_graph_emb = self.gcn_conv_list[-1](right_x,right_graph_index)
        
#         graph_match.append(self.get_attention_emb(left_graph_emb,right_graph_emb,left_x_batch,right_x_batch,left_diag_cnt,right_diag_cnt))#batch*bins 
        
#         graph_drmms_score = torch.cat(graph_match,dim=-1)
        
#         left_self_emb = self.nodes_filter_layer(left_graph_emb,left_x)
#         right_self_emb = self.nodes_filter_layer(right_graph_emb,right_x)
#         left_self_global_emb, right_self_global_emb = add_p(left_self_emb,left_x_batch).squeeze(dim=1), add_p(right_self_emb,right_x_batch).squeeze(dim=1)

#         pair_sim = self.neural_sim_layer_1(left_self_global_emb,right_self_global_emb)
        
#         return pair_sim, graph_drmms_score
    
    def forward(self,left_x,left_graph_index,right_x,right_graph_index,left_x_batch,right_x_batch,left_diag_cnt,right_diag_cnt,label=None):
        pair_sim, graph_drmms_score = self.encode(left_x,left_graph_index,right_x,right_graph_index,left_x_batch,right_x_batch,left_diag_cnt,right_diag_cnt)
        
        if self.use_glb==0:outputs =  torch.sigmoid(self.fully_connected_layer(pair_sim)) # global infos
        elif self.use_glb==1:outputs =  torch.sigmoid(self.fully_connected_layer(graph_drmms_score)) # local infos
        elif self.use_glb==2:outputs = torch.sigmoid(self.fully_connected_layer(torch.cat([pair_sim,graph_drmms_score],dim=1)))
#             outputs = self.gamma*torch.sigmoid(self.fully_connected_layer_1(pair_sim)) + (1-self.gamma)*torch.sigmoid(self.fully_connected_layer_2(graph_drmms_score))# squared distances both info                                                             
                                                                                                                      
        outputs = outputs.squeeze(dim=1)
        if label is not None:
#             loss_fct = ContrastiveLoss()
            loss_fct = nn.MSELoss()
            sim_loss = loss_fct(outputs.view(-1), label.view(-1))
            outputs = [sim_loss, outputs]
        
        return outputs
    
class EHROntologyModel(nn.Module):
    def __init__(self, config,vocab_emb,diag_voc,proce_voc,atc_voc,device):#,in_channels=100,out_channels=100
        super(EHROntologyModel,self).__init__()
        
        self.specific_embedding = nn.Parameter(torch.randn(1,config.graph_hidden_size))
        self.atc_embedding = OntologyEmbedding(atc_voc, build_atc_tree,
                                              config.hidden_size, config.graph_hidden_size,
                                              config.graph_heads)
        self.diag_embedding = OntologyEmbedding(diag_voc, build_diag_tree,
                                              config.hidden_size, config.graph_hidden_size,
                                              config.graph_heads)
        self.proce_embedding = OntologyEmbedding(proce_voc, build_proce_tree,
                                              config.hidden_size, config.graph_hidden_size,
                                              config.graph_heads)
        
#         self.all_embedding = torch.cat(
#             [self.diag_embedding(),self.proce_embedding(),self.atc_embedding()], dim=0)
#         self.all_embedding = self.all_embedding.to(device)
        self.gcn_conv_list = nn.ModuleList()
        for _ in range(config.gcn_conv_nums-1):
            self.gcn_conv_list.append(GCNConv(in_channels=config.graph_hidden_size,out_channels=config.graph_hidden_size))
        self.gcn_conv_list.append(GCNConv(in_channels=config.graph_hidden_size,out_channels=config.out_channels))
        
#         self.gcn_conv = GCNConv(in_channels=config.graph_hidden_size,out_channels=config.out_channels)
        
#         self.dropout_1 = nn.Dropout(config.dropout_rate)
#         self.dropout_2 = nn.Dropout(config.dropout_rate)
#         self.gcn_pooling = SAGPooling(in_channels=config.out_channels)
        
#         self.classifier = nn.Linear(config.out_channels*2,2)
    
    def get_final_node(self,x,batch):
        num_nodes = scatter_add(batch.new_ones(x.size(0)),batch,dim=0)
        cum_num_nodes = torch.cat([num_nodes.new_zeros(1),num_nodes.cumsum(dim=0)[:-1]],dim=0)
        return x[cum_num_nodes,:]
    
    def forward(self,left_x,left_graph_index,right_x,right_graph_index,left_x_batch,right_x_batch,label=None):
        all_embedding = torch.cat(
            [self.specific_embedding,self.diag_embedding(),self.proce_embedding(),self.atc_embedding()], dim=0)
        
        left_x = all_embedding[left_x].squeeze(dim=1)
        right_x = all_embedding[right_x].squeeze(dim=1)
        
#         left_x = self.dropout_1(left_x)
#         right_x = self.dropout_1(right_x)
        for gcn_conv in self.gcn_conv_list[:-1]:
            left_x = gcn_conv(left_x,left_graph_index)
            right_x = gcn_conv(right_x,right_graph_index)
        
        left_graph_emb = self.gcn_conv_list[-1](left_x,left_graph_index)
        right_graph_emb = self.gcn_conv_list[-1](right_x,right_graph_index)
        
#         left_graph_emb = self.gcn_conv(left_x,left_graph_index)
#         right_graph_emb = self.gcn_conv(right_x,right_graph_index)
        
#         left_graph_emb = self.dropout_1(left_graph_emb)
#         right_graph_emb = self.dropout_1(right_graph_emb)
        
        left_graph_final_emb = self.get_final_node(left_graph_emb,left_x_batch)
        
        right_graph_final_emb = self.get_final_node(right_graph_emb,right_x_batch)
        
#         left_graph_emb, left_graph_index, _ , left_x_batch , _ , _  = self.gcn_pooling(left_graph_emb,left_graph_index,batch=left_x_batch)
#         left_graph_final_emb = add_p(left_graph_emb,left_x_batch) #batch*graph_hidden_size for graph vector
    
        
#         right_graph_emb, right_graph_index, _ , right_x_batch , _ , _ = self.gcn_pooling(right_graph_emb,right_graph_index,batch=right_x_batch)
#         right_graph_final_emb = add_p(right_graph_emb,right_x_batch) #batch*graph_hidden_size    
        
#         outputs = self.classifier(torch.cat([left_graph_final_emb,right_graph_final_emb],dim=1))
        
#         if label is not None:
#             loss_fct = nn.CrossEntropyLoss(ignore_index=-1)
#             sim_loss = loss_fct(outputs.view(-1, 2), label.view(-1))
#             outputs = [sim_loss, outputs]
        
#         return outputs

        cos_sim = CosineSimilarity(eps=1e-6)
        outputs =  cos_sim(left_graph_final_emb, right_graph_final_emb)  # squared distances
        
        if label is not None:
            loss_fct = ContrastiveLoss()
            sim_loss = loss_fct(outputs.view(-1), label.view(-1))
            outputs = [sim_loss, outputs]
        
        return outputs
        

class OntologyEmbedding(nn.Module):
    def __init__(self, voc, build_tree_func,
                 in_channels=100, out_channels=100, heads=1):
        super(OntologyEmbedding, self).__init__()

        # initial tree edges
        res, graph_voc = build_tree_func(list(voc.idx2word.values()))
        stage_one_edges = build_stage_one_edges(res, graph_voc)
        stage_two_edges = build_stage_two_edges(res, graph_voc)

        self.edges1 = torch.tensor(stage_one_edges)
        self.edges2 = torch.tensor(stage_two_edges)
        self.graph_voc = graph_voc

        # construct model
        assert in_channels == heads * out_channels
        self.g = GATConv(in_channels=in_channels,
                         out_channels=out_channels,
                         heads=heads)

        # tree embedding
        num_nodes = len(graph_voc.word2idx)
        self.embedding = nn.Parameter(torch.randn(num_nodes, in_channels))

        # idx mapping: FROM leaf node in graphvoc TO voc
        self.idx_mapping = [self.graph_voc.word2idx[word[2:]]
                            for word in voc.idx2word.values()]

        self.init_params()

    def get_all_graph_emb(self):
        emb = self.embedding
        
        emb = self.g(self.g(emb, self.edges1.to(emb.device)),
                     self.edges2.to(emb.device))
        return emb

    def forward(self):
        """
        :param idxs: [N, L]
        :return:.to(emb.device)
        """
#         test_1 = self.embedding[self.edges1]
#         test_2 = self.embedding[self.edges2]
#         print(test_1)
#         print(test_2)
        emb = self.embedding

        emb = self.g(self.g(emb, self.edges1.to(emb.device)),self.edges2.to(emb.device))

        return emb[self.idx_mapping]

    def init_params(self):
        glorot(self.embedding)


class ConcatEmbeddings(nn.Module):
    """Concat rx and dx e embedding for easy access
    """

    def __init__(self, config, diag_voc, proce_voc ,atc_voc):
        super(ConcatEmbeddings, self).__init__()
        self.atc_embedding = OntologyEmbedding(atc_voc, build_atc_tree,
                                              config.hidden_size, config.graph_hidden_size,
                                              config.graph_heads)
        self.diag_embedding = OntologyEmbedding(diag_voc, build_diag_tree,
                                              config.hidden_size, config.graph_hidden_size,
                                              config.graph_heads)
        self.proce_embedding = OntologyEmbedding(proce_voc, build_proce_tree,
                                              config.hidden_size, config.graph_hidden_size,
                                              config.graph_heads)

    def forward(self, input_ids):
        emb = torch.cat(
            [self.diag_embedding(),self.proce_embedding(),self.atc_embedding()], dim=0)
        return emb[input_ids]



class DAGEmbedding(nn.Module):
    def __init__(self, voc, build_tree_func,
                 in_channels=100, out_channels=100, heads=1,level=4):
        super(DAGEmbedding, self).__init__()

        # initial tree edges
        res, graph_voc = build_tree_func(list(voc.idx2word.values()))

        ancestor_nodes,leave_nodes = build_ancestors(res, graph_voc)#node_num*depth

        self.ancestor_nodes = torch.tensor(ancestor_nodes)
        self.leave_nodes = torch.tensor(leave_nodes)
        
        self.graph_voc = graph_voc

        # tree embedding
        num_nodes = len(graph_voc.word2idx)
        
        self.level = level
        self.embedding = nn.Parameter(torch.randn(num_nodes, in_channels))
        self.atten_params = nn.Parameter(torch.randn(out_channels,1))
        
        self.atten_linear = nn.Linear(2*in_channels,out_channels)

    def forward(self):
        """
        :param idxs: [N, L]
        :return:.to(emb.device)
        """          
        ancestor_emb  = self.embedding[self.ancestor_nodes] #left_node_num*depth*dim
        left_emb = self.embedding[self.leave_nodes]
        

        atten_weight = torch.matmul(torch.tanh(self.atten_linear(torch.cat([ancestor_emb,left_emb],dim=-1))),self.atten_params).squeeze(dim=-1)#left_node_num*depth
        attened_leaf_emb = torch.matmul(F.softmax(atten_weight,dim=-1),ancestor_emb).sum(dim=1)#left_node_num*dim

        return attened_leaf_emb


    
if __name__=='__main__':
    loss_fct = ContrastiveLoss()
    print(loss_fct(torch.Tensor([0.2]),torch.Tensor([1])))
#     model = TestModel().cuda()
#     print(model(torch.LongTensor([[0,3,2],[5,7,8]]).cuda()))