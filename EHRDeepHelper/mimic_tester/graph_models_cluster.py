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
    

    
class EHROntologyModel(nn.Module):
    def __init__(self, config,vocab_emb,image_vocab_emb,diag_voc,proce_voc,atc_voc,device):#,in_channels=100,out_channels=100
        super(EHROntologyModel,self).__init__()

        
        self.atc_embedding = OntologyEmbedding(atc_voc, build_atc_tree,
                                              config.hidden_size, config.hidden_size,
                                              config.graph_heads)
        self.diag_embedding = OntologyEmbedding(diag_voc, build_diag_tree,
                                              config.hidden_size, config.hidden_size,
                                              config.graph_heads)
        self.proce_embedding = OntologyEmbedding(proce_voc, build_proce_tree,
                                              config.hidden_size, config.hidden_size,
                                              config.graph_heads)

        self.desc_embedding = nn.Parameter(vocab_emb)
        glorot(self.desc_embedding)
        
        self.image_embedding = nn.Parameter(image_vocab_emb)
        glorot(self.image_embedding)
        
        
        self.desc_linear = nn.Linear(config.desc_emb_size,config.hidden_size)
        self.image_linear = nn.Linear(config.img_emb_size,config.hidden_size)
        
        """if config.use_emb == 2:#2 for three embeddings  1 for multimodal embeddings  0 for ontology embeddings        
            self.concat_emb_size = 3*config.hidden_size            
        elif config.use_emb == 1 or config.use_emb == 5 or config.use_emb == 6:
            self.concat_emb_size = 2*config.hidden_size
        else:
            self.concat_emb_size = config.hidden_size"""
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
    
        self.global_tensor_dim =  self.out_channels
        self.neural_sim_layer_1 = NeuralTensorModule(self.global_tensor_dim,config.pair_neurons)

        self.nodes_filter_layer = NodeFilterModule(self.out_channels)
        self.dropout = nn.Dropout(config.hidden_dropout_prob)
        
        self.classifier = nn.Linear(config.pair_neurons,2)
        

    def repeat_global_emb(self,global_emb,batch,nodes_emb):
        num_nodes = scatter_add(batch.new_ones(nodes_emb.size(0)),batch,dim=0).cpu().numpy()
        global_emb_split = torch.split(global_emb,1,dim=0) # split the global embedding along the batch_size axis
        assert len(global_emb_split)==len(num_nodes)
        
        global_emb_ls = []
        for i in range(len(global_emb_split)):
            global_emb_ls.append(global_emb_split[i].repeat(num_nodes[i],1))
        global_emb_mat = torch.cat(global_emb_ls,dim=0)#num_nodes*size
        return global_emb_mat                
    

    def encode(self,all_embedding,left_x,left_graph_index,left_x_batch):       
        left_onto_x = all_embedding[left_x].squeeze(dim=1)        
        left_desc_x = self.desc_linear(self.desc_embedding[left_x].squeeze(dim=1))
        left_img_x = self.image_linear(self.image_embedding[left_x].squeeze(dim=1))
        
        if self.use_emb == 2:        
            left_x = left_onto_x+left_desc_x+left_img_x
        elif self.use_emb == 1:
            left_x = left_desc_x+left_img_x
        elif self.use_emb == 0:
            left_x = left_onto_x
        elif self.use_emb == 3:
            left_x =  left_img_x
        elif self.use_emb == 4:
            left_x = left_desc_x
        elif self.use_emb == 5:
            left_x = left_onto_x+left_desc_x
        elif self.use_emb == 6:
            left_x = left_onto_x+left_img_x

        for gcn_conv in self.gcn_conv_list[:-1]:
            left_x = F.dropout(F.elu(gcn_conv(left_x,left_graph_index)))

        left_graph_emb = self.gcn_conv_list[-1](left_x,left_graph_index)   
        left_self_emb = self.nodes_filter_layer(left_graph_emb,left_x)
        left_self_global_emb = add_p(left_self_emb,left_x_batch).squeeze(dim=1)       
              
        return left_self_global_emb
     
    
    def forward(self,left_x,left_graph_index,left_x_batch,right_x=None,right_graph_index=None,right_x_batch=None,label=None):
        all_embedding = torch.cat(
            [self.diag_embedding(),self.proce_embedding(),self.atc_embedding()], dim=0)
        
        #get the patient embedding
        if right_x is None:   
            return self.encode(all_embedding,left_x,left_graph_index,left_x_batch)
        
        left_self_global_emb,right_self_global_emb = self.encode(all_embedding,left_x,left_graph_index,left_x_batch), self.encode(all_embedding,right_x,right_graph_index,right_x_batch)
             
        pair_sim = self.neural_sim_layer_1(left_self_global_emb,right_self_global_emb)
        outputs =  self.classifier(self.dropout(pair_sim)) # global infos                                       
        
        if label is not None:
            loss_fct = nn.CrossEntropyLoss()
            sim_loss = loss_fct(outputs.view(-1,2), label.view(-1))
            outputs = [sim_loss, outputs]
        
        return outputs
    
        

class OntologyEmbedding(nn.Module):
    def __init__(self, voc, build_tree_func,
                 in_channels=100, out_channels=100, heads=1):
        super(OntologyEmbedding, self).__init__()

        # initial tree edges

        # print(voc.idx2word.values())

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