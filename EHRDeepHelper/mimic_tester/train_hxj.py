
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
import datetime


logging.basicConfig(format='%(asctime)s - %(levelname)s - %(name)s -   %(message)s',
                    datefmt='%m/%d/%Y %H:%M:%S',
                    level=logging.INFO)
logger = logging.getLogger(__name__)

def test_query(ehr_file,processed_file,batch_size,dtype,add_admission):
    base_dir = '../self_dataset/'
    vocab_file = base_dir+'vocab.pkl'
    tokenizer = EHRTokenizer(vocab_file,add_admission=add_admission)
    
    query_dataloader, query_case_ids,_ = construct_query_dataloder(tokenizer,processed_file,ehr_file,batch_size,dtype='onto')
    
    return query_dataloader,query_case_ids

def load_dataset(train_processed_file,valid_processed_file,test_processed_file,batch_size,dtype,add_admission):
    base_dir = '../self_dataset/'
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

    train_ehr_file,train_label_file = base_dir+'train_admissions.csv',base_dir+'train_label.csv'
    train_dataloader = construct_dataloader(train_processed_file,tokenizer,train_ehr_file,train_label_file,batch_size = batch_size,dtype=dtype)
    
       
    valid_ehr_file,valid_label_file = base_dir+'valid_admissions.csv',base_dir+'valid_label.csv'
    valid_dataloader = construct_dataloader(valid_processed_file,tokenizer,valid_ehr_file,valid_label_file,shuffle=False,batch_size = batch_size,dtype=dtype)
    
      
    test_ehr_file,test_label_file = base_dir+'test_admissions.csv',base_dir+'test_label.csv'
    test_dataloader = construct_dataloader(test_processed_file,tokenizer,test_ehr_file,test_label_file,shuffle=False,batch_size = batch_size,dtype=dtype)
    
    
    return vocab_emb,tokenizer,train_dataloader,valid_dataloader,test_dataloader,image_vocab_emb

#from the best save model, report our result on the dataset
def test(model,test_dataloader,model_file,device):
    model_state_dict = torch.load(model_file)
    model.load_state_dict(model_state_dict)
    model.to(device)
    
    model.eval()
    all_predict_labels,all_true_labels = [],[]
    for batch in tqdm(test_dataloader,desc='Testing'):
        with torch.no_grad():
            outputs = model(left_x=batch.x_left.to(device),left_graph_index=batch.edge_index_left.long().to(device),right_x=batch.x_right.to(device),right_graph_index=batch.edge_index_right.long().to(device),\
             left_x_batch=batch.x_left_batch.to(device),right_x_batch=batch.x_right_batch.to(device),\
                         left_diag_cnt = batch.left_diag_cnt.to(device), right_diag_cnt = batch.right_diag_cnt.to(device),)
#             _, predict_labels = torch.max(outputs,dim=1)
            predict_labels = (outputs>0.5).long()
            predict_labels = predict_labels.detach().cpu().numpy()
            true_labels = batch.y.detach().cpu().numpy()

            all_predict_labels.append(predict_labels)
            all_true_labels.append(true_labels)
    
    test_ev_metrics = report_metrics(np.concatenate(all_true_labels,axis=0),np.concatenate(all_predict_labels,axis=0))
    for key,val in test_ev_metrics.items():
        print('the',key,'score is',val)
        
    

def main():
    parser = argparse.ArgumentParser()#
    parser.add_argument('--dtype',type=str,default='undirect_onto',help='the model type: onto no_onto test.')
    parser.add_argument('--do_query',default=False,action='store_true',help='whether to do the query case in our model.')
    parser.add_argument('--query_data',type=str,required=False,help='if do_query is true, you need to set the query_data for your query admissions file. ')
    parser.add_argument('--add_admission',default=False,action='store_true',help='whether to add the admission node.')
    parser.add_argument('--use_cross_atten',default=False,action='store_true',help='whether to use cross attention in our model.')
    parser.add_argument('--do_train',default=False,action='store_true',help='whether to train the model.')
    parser.add_argument('--do_eval',default=False,action='store_true',help='whether to test the performance on valiation dataset.')
    parser.add_argument('--do_test',default=False,action='store_true',help='whether to test the performance on test dataset.')
    parser.add_argument('--bert_finetune',default=False,action='store_true',help='whether to use bert to finetune our model for text embedding.')
    parser.add_argument('--learning_rate',type=float,default=0.001,help='the initial learning rate for Adam.')
    parser.add_argument('--intermediate_size',type=int,default=100,help='the intermediate size for transformer.')
    parser.add_argument('--num_heads',type=int,default=4,help='the num of heads for transformer.')
    parser.add_argument('--hidden_dropout_prob',type=float,default=0.2,help='the droput rate for self attention transformer.')
    parser.add_argument('--batch_size',type=int,default=16,help='batch size for train dataset')
    parser.add_argument('--self_attn_nums',type=int,default=1,help='the number of self transformer for meta path enrichment in our model')
    parser.add_argument('--gcn_conv_nums',type=int,default=1,help='the number of gcn convoluation  layer for our model')
    parser.add_argument('--num_train_epochs',type=int,default=10,help='the number of training epoch')
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
    parser.add_argument('--match',type=str,default='knrm',help='the method for transforming matching information: knrm, drmm')
#     parser.add_argument('--data_dir',type=str,required=True,help='the processed data  path for train_data.pt valid_data.pt and test_data.pt')
    parser.add_argument('--train_data',type=str,required=True,help='the processed data  path for train_data.pt ')
    parser.add_argument('--test_data',type=str,required=True,help='the processed data  path for test_data.pt')
    parser.add_argument('--valid_data',type=str,required=True,help='the processed data  path for valid_data.pt')
    parser.add_argument('--query_save_data',type=str,required=False,help='the processed data  path for query_data.pt, use with do_query ')
    parser.add_argument("--output_dir",
                        default='saved/',
                        type=str,
                        required=False,
                        help="The output directory where the model checkpoints will be written.")
    args = parser.parse_args()
    
    random.seed(args.seed)
    np.random.seed(args.seed)
    torch.manual_seed(args.seed)
   
    
    device = torch.device('cuda:1' if torch.cuda.is_available() else 'cpu') 
    # device = torch.device('cpu')
    
    if torch.cuda.is_available():
        torch.cuda.manual_seed(args.seed)
        torch.cuda.manual_seed_all(args.seed)
    
    print('loading dataset')
    
    
#     tokenizer, train_dataset = load_dataset() 
#     train_dataloader = DataLoader(train_dataset, sampler=RandomSampler(train_dataset), batch_size=1)
#     print(train_dataset[0], train_dataset[1], train_dataset[2])
#     dtype = 'onto' if args.use_ontology else 'no_onto'
#     dtype = 'test'
#     dtype = args.dtype
    
    # code_id_to_desc: for each code get the description
    vocab_emb,tokenizer,train_dataloader,valid_dataloader,test_dataloader,image_vocab_emb = load_dataset(args.train_data,args.valid_data,args.test_data,args.batch_size,dtype=args.dtype,add_admission=args.add_admission)
    """
    #drop for the usability
    desc_bert_inputs = None
    if args.bert_finetune:
        bert_tokenizer = BertTokenizer.from_pretrained("bionlp/bluebert_pubmed_mimic_uncased_L-12_H-768_A-12")
        batch_encoding = bert_tokenizer(
            code_id_to_desc,
            max_length=10,
            padding="max_length",
            truncation=True,
        )
        desc_bert_inputs = {k:torch.tensor(batch_encoding[k],dtype=torch.long).to(device) for k in batch_encoding}"""
    
    args.vocab_size = len(tokenizer.vocab.word2idx)
    
    
    diag_voc,proce_voc,pres_voc = tokenizer.diag_voc, tokenizer.proce_voc, tokenizer.atc_voc
    
#     if dtype=='onto':
#         model = EHROntologyModel(args,diag_voc,proce_voc,pres_voc,device).to(device)
#     elif dtype=='no_onto':
#         model = EHRModel(args).to(device)
#     elif dtype=='test':
#         model = TestModel(args).to(device)
    if args.add_admission:
        model = EHROntologyModel(args,vocab_emb,diag_voc,proce_voc,pres_voc,device).to(device)
    elif args.match=='knrm':
        print('use knrm')
        model = KNRMEHROntologyModel(args,vocab_emb,image_vocab_emb,diag_voc,proce_voc,pres_voc,device).to(device)
    elif args.match=='drmm':
        print('use drmm')
        model = DRMMEHROntologyModel(args,vocab_emb,image_vocab_emb,diag_voc,proce_voc,pres_voc,device).to(device)
    elif args.match=='gram':
        print('for baseline gram')
        model = GRAMModel(args,vocab_emb,diag_voc,proce_voc,pres_voc,device).to(device)
    
#     print([k for k,v in model.named_parameters()])
    
    optimizer = Adam(model.parameters(),lr=args.learning_rate)
    os.makedirs(args.output_dir,exist_ok=True)
    
    model_to_save = model.module if hasattr(model,'module') else model
    output_model_file = os.path.join(args.output_dir,'pytorch_model.bin')
    
#     query_dataloader, query_case_ids = test_query(args.query_data,args.query_save_data,args.batch_size,args.dtype,args.add_admission)

    if args.do_query:
        
        def generate_tab(query_data):
            import pandas as pd
            import random
            test_admission_df = pd.read_csv('../self_dataset/test_admissions.csv')
            similar_disease, other_disease = ['410','411','412','413','414'], []
            high_fre_disease = '41401'
            for sim_dis in similar_disease:
                for dis in list(set(test_admission_df['disease'])):#groupby('disease').size().reset_index(name='count').sort_values(['count'],ascending=False
                    if dis.startswith(sim_dis) and dis!=high_fre_disease:
                        other_disease.append(dis)

            # other_disease = ['41402','41011','41189']
            chosen_admission_df = test_admission_df[test_admission_df['disease']==high_fre_disease]
            close_chosen_admission_df = test_admission_df[test_admission_df.disease.isin(other_disease)]
            other_disease.append(high_fre_disease)
            not_chosen_admission_df = test_admission_df[~test_admission_df.disease.isin(other_disease)]
            assert len(test_admission_df) == len(chosen_admission_df)+len(not_chosen_admission_df)+len(close_chosen_admission_df)

            import random
            admissions = random.sample(range(len(chosen_admission_df)),4)
            base_df = chosen_admission_df.iloc[admissions,:]
            close_admissions = random.sample(range(len(close_chosen_admission_df)),2)
            close_df = close_chosen_admission_df.iloc[close_admissions,:]
            not_admissions = random.sample(range(len(not_chosen_admission_df)),5)
            other_df = not_chosen_admission_df.iloc[not_admissions,:]
            all_df = pd.concat([base_df,close_df,other_df])

            all_df.to_csv(query_data,index=False)
            
        model_state_dict = torch.load(output_model_file)
        model.load_state_dict(model_state_dict)
        model.to(device)
        model.eval()
        query_epochs = 1 #100 for select the best case study
        
        for _ in range(query_epochs):
            query_dataloader, query_case_ids = test_query(args.query_data,args.query_save_data,args.batch_size,args.dtype,args.add_admission)

            all_predict_labels,all_predict_scores,all_true_labels = [],[],[]
            for batch in tqdm(query_dataloader,desc='Querying'):
                with torch.no_grad():
                    outputs = model(left_x=batch.x_left.to(device),left_graph_index=batch.edge_index_left.long().to(device),right_x=batch.x_right.to(device),right_graph_index=batch.edge_index_right.long().to(device),\
                     left_x_batch=batch.x_left_batch.to(device),right_x_batch=batch.x_right_batch.to(device),\
                                 left_diag_cnt = batch.left_diag_cnt.to(device), right_diag_cnt = batch.right_diag_cnt.to(device),)
                    predict_scores = outputs.detach().cpu().numpy()
                    predict_labels = (outputs>0.5).long()
                    predict_labels = predict_labels.detach().cpu().numpy()
                    true_labels = batch.y.detach().cpu().numpy()

                    all_predict_labels.extend(predict_labels)
                    all_predict_scores.extend(predict_scores)
                    all_true_labels.extend(true_labels)
            
            print(all_predict_labels,all_true_labels)
            
            sorted_index = sorted(range(len(all_predict_scores)),key=lambda x:all_predict_scores[x],reverse=True)
            cur_flag = True
            if query_epochs == 1:break        
            for idx in sorted_index[:3]:
                if idx not in range(3):
                    cur_flag = False
                if all_predict_labels[idx]!=1:
                    cur_flag = False
            if cur_flag:break
            generate_tab(args.query_data)
        if cur_flag:    
            for s_i in sorted_index:
                print(query_case_ids[s_i],all_predict_scores[s_i],all_predict_labels[s_i])
            print(query_case_ids)
        print('query done')
        return
      
    if args.do_train:
        global_step,valid_accu_best = 0, 0
        writer = SummaryWriter(args.output_dir)
        for _ in trange(int(args.num_train_epochs),desc='training epoch'):
        
            start_time = datetime.datetime.now()
            #begin to train
            tr_loss,nb_tr_steps = 0,0
            epoch_iterator = tqdm(train_dataloader, leave=False, desc='Training')
            logger.info('***Running training***')
            logger.info("num of examples = %d",len(epoch_iterator)*args.batch_size)

            all_p_labels = []
            all_p_preds = []
            for step,batch in enumerate(epoch_iterator):
                model.train()
                loss, p_output = model(left_x=batch.x_left.to(device),left_graph_index=batch.edge_index_left.long().to(device),right_x=batch.x_right.to(device),right_graph_index=batch.edge_index_right.long().to(device),\
                     left_x_batch=batch.x_left_batch.to(device),right_x_batch=batch.x_right_batch.to(device),\
                         left_diag_cnt = batch.left_diag_cnt.to(device), right_diag_cnt = batch.right_diag_cnt.to(device), label=batch.y.float().to(device))#for mse loss
                loss.backward() 
    #             _, p_labels = torch.max(p_output,dim=1)
                p_labels = (p_output>0.5).long()

                all_p_preds.append(p_labels.detach().cpu().numpy())
                all_p_labels.append(batch.y.detach().cpu().numpy())


                tr_loss += loss.item()
                nb_tr_steps += 1

                epoch_iterator.set_postfix(loss='%.4f'%(tr_loss/nb_tr_steps))

                optimizer.step()
                optimizer.zero_grad()

            writer.add_scalar('train/loss', tr_loss/nb_tr_steps,global_step)
            train_ev_metrics = report_metrics(np.concatenate(all_p_labels,axis=0),np.concatenate(all_p_preds,axis=0))
            writer.add_scalar('train/{}'.format('accu'),train_ev_metrics['accu'],global_step)
            for name, param in model.named_parameters():
                writer.add_histogram(name, param, global_step)

            global_step += 1

            total_training_time = (datetime.datetime.now() - start_time).total_seconds()
            print('预训练时间:', total_training_time)

            val_loss,nb_val_steps = 0,0

            if args.do_eval:
                #begin to valid
                logger.info('***Running eval***')
                logger.info("num of examples = %d",len(valid_dataloader)*args.batch_size)
                model.eval()
                all_predict_labels = []
                all_true_labels = []
                for batch in tqdm(valid_dataloader,desc='Evaluating'):
                    with torch.no_grad():
                        v_loss,outputs = model(left_x=batch.x_left.to(device),left_graph_index=batch.edge_index_left.long().to(device),right_x=batch.x_right.to(device),right_graph_index=batch.edge_index_right.long().to(device),\
                         left_x_batch=batch.x_left_batch.to(device),right_x_batch=batch.x_right_batch.to(device),\
                         left_diag_cnt = batch.left_diag_cnt.to(device), right_diag_cnt = batch.right_diag_cnt.to(device), label=batch.y.to(device))
    #                     _, predict_labels = torch.max(outputs,dim=1)
                        predict_labels = (outputs>0.5).long()

                        val_loss += v_loss.item()
                        nb_val_steps += 1

                        predict_labels = predict_labels.detach().cpu().numpy()
                        true_labels = batch.y.detach().cpu().numpy()


                        all_predict_labels.append(predict_labels)
                        all_true_labels.append(true_labels)
                valid_ev_metrics = report_metrics(np.concatenate(all_true_labels,axis=0),np.concatenate(all_predict_labels,axis=0))
                writer.add_scalar('eval/loss', val_loss/nb_val_steps,global_step)

                for key,val in valid_ev_metrics.items():
                    writer.add_scalar('eval/{}'.format(key),val,global_step)

                if valid_ev_metrics['accu'] > valid_accu_best:
                    valid_accu_best = valid_ev_metrics['accu']
                    torch.save(model_to_save.state_dict(),output_model_file)
                
    if args.do_test:        
        print('begin to report the result on test dataset')
        print("num of examples =",len(test_dataloader)*args.batch_size)
        ## 测试代码
        print(test_dataloader)
        test(model,test_dataloader,output_model_file,device)
                
            
    
if __name__=='__main__':
    main()
    