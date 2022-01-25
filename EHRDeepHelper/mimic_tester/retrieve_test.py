from __future__ import absolute_import,division,print_function
import os
import logging
from util import EHRTokenizer,construct_dataloader,report_metrics,load_ehr_infos,EHRPairData,load_codes_embeddings, load_image_embeddings, construct_query_dataloder
import argparse
import numpy as np
import random
import torch
import torch.nn as nn
from torch_geometric.data import InMemoryDataset
# from torch.utils.data import DataLoader, RandomSampler, SequentialSampler
from graph_models import EHRModel,EHROntologyModel,TestModel,KNRMEHROntologyModel,DRMMEHROntologyModel,GRAMModel
from tqdm import tqdm, trange
from tensorboardX import SummaryWriter
from torch.optim import Adam, SGD


logging.basicConfig(format='%(asctime)s - %(levelname)s - %(name)s -   %(message)s',
                    datefmt='%m/%d/%Y %H:%M:%S',
                    level=logging.INFO)
logger = logging.getLogger(__name__)

def test_query(ehr_file,processed_file,batch_size,dtype,add_admission):
    base_dir = '/home/lf/桌面/MIMIC全/self_dataset/'
    vocab_file = base_dir+'vocab.pkl'
    tokenizer = EHRTokenizer(vocab_file,add_admission=add_admission)
    
    diag_embeddings,proce_embeddings,atc_embeddings = load_codes_embeddings()
    vocab_emb = np.random.randn(len(tokenizer.vocab.word2idx),300)
    for idx,word in tokenizer.vocab.idx2word.items():
        w_type = word[0]
        if w_type=='d' and word[2:] in diag_embeddings:vocab_emb[idx] = diag_embeddings[word[2:]] 
        elif w_type=='p' and word[2:] in proce_embeddings:vocab_emb[idx] = proce_embeddings[word[2:]]
        elif w_type=='a' and word[2:] in atc_embeddings:vocab_emb[idx] = atc_embeddings[word[2:]]
    vocab_emb = torch.tensor(vocab_emb,dtype=torch.float)
    
    diag_embeddings,proce_embeddings,atc_embeddings = load_image_embeddings()
    image_vocab_emb = np.random.randn(len(tokenizer.vocab.word2idx),2048)
    for idx,word in tokenizer.vocab.idx2word.items():
        w_type = word[0]
        if w_type=='d' and word[2:] in diag_embeddings:image_vocab_emb[idx] = diag_embeddings[word[2:]] 
        elif w_type=='p' and word[2:] in proce_embeddings:image_vocab_emb[idx] = proce_embeddings[word[2:]]
        elif w_type=='a' and word[2:] in atc_embeddings:image_vocab_emb[idx] = atc_embeddings[word[2:]]
    image_vocab_emb = torch.tensor(image_vocab_emb,dtype=torch.float)
    
    query_dataloader, query_case_ids, query_id = construct_query_dataloder(tokenizer,processed_file,ehr_file,batch_size,dtype='onto') 
    
    return vocab_emb,tokenizer,image_vocab_emb,query_dataloader,query_case_ids,query_id


    

def main():
    parser = argparse.ArgumentParser()#
    parser.add_argument('--dtype',type=str,default='onto',help='the model type: onto no_onto test.')
    parser.add_argument('--add_admission',default=False,action='store_true',help='whether to add the admission node.')
    parser.add_argument('--use_cross_atten',default=False,action='store_true',help='whether to use cross attention in our model.')
    parser.add_argument('--intermediate_size',type=int,default=100,help='the intermediate size for transformer.')
    parser.add_argument('--num_heads',type=int,default=4,help='the num of heads for transformer.')
    parser.add_argument('--hidden_dropout_prob',type=float,default=0.1,help='the droput rate for self attention transformer.')
    parser.add_argument('--batch_size',type=int,default=256,help='batch size for train dataset')
    parser.add_argument('--self_attn_nums',type=int,default=1,help='the number of self transformer for meta path enrichment in our model')
    parser.add_argument('--gcn_conv_nums',type=int,default=1,help='the number of gcn convoluation  layer for our model')
    parser.add_argument('--emb_size',type=int,default=100,help='the vocab embedding size')
    parser.add_argument('--hidden_size',type=int,default=100,help='the ontology embedding hidden size')
    parser.add_argument('--desc_emb_size',type=int,default=300,help='the bert embedding hidden size')#
    parser.add_argument('--img_emb_size',type=int,default=2048,help='the image embedding hidden size')
    parser.add_argument('--graph_hidden_size',type=int,default=100,help='the graph embedding hidden size')
    parser.add_argument('--graph_heads',type=int,default=1,help='the graph heads')
#     parser.add_argument('--hidden_dropout_prob',type=float,default=0.1,help='the dropout rate in our model')
    parser.add_argument('--out_channels',type=int,default=300,help='the output channels for graph model')
    parser.add_argument('--seed',type=int,default=1002,help='random seed for initialization')
    parser.add_argument('--use_attn',type=int,default=2,help='use which attention module, 1 for global cross attention, 2 for max softmax attention. ')
    parser.add_argument('--use_glb',type=int,default=2,help='use_glb  0 for global info, 1 for local info, and 2 for both')
    parser.add_argument('--use_emb',type=int,default=2,help='use_emb  0 for onto info, 1 for multimodal info, 2 for both, 3 for image info, 4 for text info, 5 for onto and text, 6 for onto and image')
    parser.add_argument('--bins',type=int,default=20,help='the bins for drmm kernels')
    parser.add_argument('--pair_neurons',type=int,default=20,help='the neurons for global embedding')
    parser.add_argument('--use_conv',type=str,default='gcn',help='the convolution method : gcn, gin, gat, gated_conv')
    parser.add_argument('--match',type=str,default='drmm',help='the method for transforming matching information: knrm, drmm')
    parser.add_argument('--query_data',type=str,required=False,help='if do_query is true, you need to set the query_data for your query admissions file. ')
    parser.add_argument('--query_save_data',type=str,required=False,help='the processed data  path for query_data.pt, use with do_query ')
    parser.add_argument("--output_dir",
                        default='/home/lf/MIMIC全/mimic_tester/output/test_4/',
                        type=str,
                        required=False,
                        help="The output directory where the model checkpoints will be written.")
    args = parser.parse_args()
    
    random.seed(args.seed)
    np.random.seed(args.seed)
    torch.manual_seed(args.seed)
   
    
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu') 
    
    if torch.cuda.is_available():
        torch.cuda.manual_seed(args.seed)
        torch.cuda.manual_seed_all(args.seed)
    
    print('loading dataset')
    
    vocab_emb,tokenizer,image_vocab_emb,query_dataloader,query_case_ids,query_id = test_query(args.query_data, args.query_save_data,args.batch_size,args.dtype,args.add_admission)
    args.vocab_size = len(tokenizer.vocab.word2idx)
    
    
    diag_voc,proce_voc,pres_voc = tokenizer.diag_voc, tokenizer.proce_voc, tokenizer.atc_voc    

    model = DRMMEHROntologyModel(args,vocab_emb,image_vocab_emb,diag_voc,proce_voc,pres_voc,device).to(device)

    os.makedirs(args.output_dir,exist_ok=True)
    
    model_to_save = model.module if hasattr(model,'module') else model
    output_model_file = os.path.join(args.output_dir,'pytorch_model.bin')
    

    all_predict_labels,all_predict_scores = [],[]
    for batch in tqdm(query_dataloader,desc='Querying'):
        with torch.no_grad():
            outputs = model(left_x=batch.x_left.to(device),left_graph_index=batch.edge_index_left.long().to(device),right_x=batch.x_right.to(device),right_graph_index=batch.edge_index_right.long().to(device),\
             left_x_batch=batch.x_left_batch.to(device),right_x_batch=batch.x_right_batch.to(device),\
                         left_diag_cnt = batch.left_diag_cnt.to(device), right_diag_cnt = batch.right_diag_cnt.to(device),)
            predict_scores = outputs.detach().cpu().numpy()
            predict_labels = (outputs>0.5).long()
            predict_labels = predict_labels.detach().cpu().numpy()

            all_predict_labels.extend(predict_labels)
            all_predict_scores.extend(predict_scores)

    sorted_index = sorted(range(len(all_predict_scores)),key=lambda x:all_predict_scores[x],reverse=True)

    for s_i in sorted_index:
        print(query_id, query_case_ids[s_i],all_predict_scores[s_i],';',end='')

      
                
            
    
if __name__=='__main__':
    main()
    