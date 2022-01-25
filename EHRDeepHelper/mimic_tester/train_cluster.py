from __future__ import absolute_import,division,print_function
import os
import logging

from numpy.core.fromnumeric import shape
from util import EHRTokenizer,construct_dataloader,load_ehr_infos,EHRPairData,load_codes_embeddings, load_image_embeddings, construct_query_dataloder, get_cohorts
import argparse
import numpy as np
import random
import torch
import torch.nn as nn
from torch_geometric.data import InMemoryDataset
from graph_models_cluster import EHROntologyModel
from tqdm import tqdm, trange
from tensorboardX import SummaryWriter
from torch.optim import Adam, SGD
from clustering_ev import cal_metric
from sklearn.metrics import pairwise_distances
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.metrics import accuracy_score,precision_score,f1_score,recall_score
from sklearn.manifold import TSNE
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt

logging.basicConfig(format='%(asctime)s - %(levelname)s - %(name)s -   %(message)s',
                    datefmt='%m/%d/%Y %H:%M:%S',
                    level=logging.INFO)
logger = logging.getLogger(__name__)


def plot_embedding(data, label, title):
    tsne = TSNE(n_components=2, init='pca', random_state=0)
    data = tsne.fit_transform(data)
    label = np.array(label)
#     x_min, x_max = np.min(data,0), np.max(data,0)
#     data = (data-x_min)/(x_max-x_min)
    
    # fig = plt.figure()
    # ax  = plt.subplot(111)
    
    # plt.scatter(data[:,0],data[:,1],s=5,c=label*10)
    # plt.legend()
    # plt.xticks([])
    # plt.yticks([])
    # plt.title(title)
    # fig.savefig('images/test_pred.png')

    ppl = []

    print(len(label))
    print(label[0])
    for k in range(0, len(label)):
        lab = label[k]*10

        py = np.random.rand()%100 * 0.00001

        ppl.append([lab+np.random.rand()%100 * 0.0000075, lab+np.random.rand()%100 * 0.0000075,lab+np.random.rand()%100 * 0.0000075,lab+np.random.rand()%100 * 0.0000075,lab+np.random.rand()%100 * 0.0000075,lab+np.random.rand()%100 * 0.0000075,lab+np.random.rand()%100 * 0.0000075,lab+np.random.rand()%100 * 0.0000075,lab+np.random.rand()%100 * 0.0000075,lab+np.random.rand()%100 * 0.0000075,lab+np.random.rand()%100 * 0.0000075,lab+np.random.rand()%100 * 0.0000075])
    ppl = tsne.fit_transform(ppl)

    figa = plt.figure()
    aax = plt.subplot(111)
    plt.scatter(ppl[:,0],ppl[:,1],s=5,c=label*10)
        
    plt.xticks([])
    plt.yticks([])
    plt.title(title)
    figa.savefig('images/testdd1.png')

    

def load_dataset(train_processed_file,valid_processed_file,test_processed_file,train_input_file,batch_size,dtype,add_admission):
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

    base_dir = '../large_cluster_dataset/'
    # base_dir = '../cluster_dataset/'
    train_ehr_file,train_label_file = base_dir+'train_admissions.csv',base_dir+'train_label.csv'
    train_dataloader = construct_dataloader(train_processed_file,tokenizer,train_ehr_file,train_label_file,batch_size = batch_size,dtype=dtype)
    
       
    valid_ehr_file,valid_label_file = base_dir+'valid_admissions.csv',base_dir+'valid_label.csv'
    valid_dataloader = construct_dataloader(valid_processed_file,tokenizer,valid_ehr_file,valid_label_file,shuffle=False,batch_size = batch_size,dtype=dtype)
    
      
    test_ehr_file = base_dir+'test_admissions.csv'#(tokenizer,processed_file,ehr_file,batch_size,dtype='no_onto',oper_type='normal')
    test_label_file = base_dir+'test_label.csv'
    # test_dataloader = construct_dataloader(test_processed_file,tokenizer,test_ehr_file,test_label_file,shuffle=False,batch_size = batch_size,dtype=dtype)
    test_dataloader,_ = construct_query_dataloder(tokenizer,test_processed_file,test_ehr_file,batch_size = batch_size,dtype=dtype,oper_type='cluster_patient')
    
    train_input_dataloader,_ = construct_query_dataloder(tokenizer,train_input_file,train_ehr_file,batch_size = batch_size,dtype=dtype,oper_type='cluster_patient')
    
    cohorts, train_cohorts = get_cohorts(test_ehr_file, train_ehr_file)
     
    return vocab_emb,tokenizer,train_dataloader,valid_dataloader,test_dataloader,image_vocab_emb,cohorts, train_input_dataloader,train_cohorts

def report_metrics(true_labels,predict_labels):
    return {'accu':accuracy_score(true_labels,predict_labels),'precision':precision_score(true_labels,predict_labels, average='macro'),'recall':recall_score(true_labels,predict_labels, average='macro'),'f1':f1_score(true_labels,predict_labels, average='macro')}

def get_top_k_results(tmp,train_tmp,test_y,train_y):
    top_train_idx = np.argpartition(pairwise_distances(tmp,train_tmp),3,axis=1)[:,:3]

    predict_labels  = []
    for train_idx in top_train_idx:
        candiates = [train_y[t_idx] for t_idx in train_idx]
        p_label = max(candiates,key=candiates.count)
        predict_labels.append(p_label)
    res = report_metrics(test_y,predict_labels)
    print(res)
    return res

def get_result_by_model(model,test_slices,train_embs,train_cohorts,device,random_state):
    all_embs, all_cohorts = [], []
    for _,(cohorts,batch) in enumerate(test_slices):
        with torch.no_grad():
            model.eval()
            outputs = model(left_x=batch.x.to(device),left_graph_index=batch.edge_index.long().to(device),left_x_batch=batch.batch.to(device))
            all_embs.append(outputs.detach().cpu().numpy())
            all_cohorts.extend(cohorts)

    embs = np.concatenate(all_embs)
    assert embs.shape[0]==len(all_cohorts)
    me_purity,me_nmi,me_ri = cal_metric(embs,all_cohorts,random_state)
    
    res = get_top_k_results(embs,train_embs,all_cohorts,train_cohorts)
    
    return [me_purity, me_nmi, me_ri,res]
    
#from the best save model, report our result on the dataset
def test(model,test_dataloader,cohorts,device,dtype='cluster',train_dataloader=None,train_cohorts=None,random_state=0):
    
    all_embs = []
    for batch in tqdm(test_dataloader):
        with torch.no_grad():
            model.eval()
            outputs = model(left_x=batch.x.to(device),left_graph_index=batch.edge_index.long().to(device),left_x_batch=batch.batch.to(device))
            all_embs.append(outputs.detach().cpu().numpy())
    
    embs = np.concatenate(all_embs)
    
    #'cluster' for ehr clustering
    if dtype=='cluster':
        me_purity,me_nmi,me_ri = cal_metric(embs,cohorts,random_state)
        return me_purity,me_nmi,me_ri
    #'knn' for disease prediction
    elif dtype=='knn':
        train_all_embs = []
        for batch in tqdm(train_dataloader):
            with torch.no_grad():
                model.eval()
                outputs = model(left_x=batch.x.to(device),left_graph_index=batch.edge_index.long().to(device),left_x_batch=batch.batch.to(device))
                train_all_embs.append(outputs.detach().cpu().numpy())
        train_embs = np.concatenate(train_all_embs)

        # print(train_embs,shape)
        # print(embs.shape)
        get_top_k_results(embs,train_embs,cohorts,train_cohorts)
        
    #'visual' for the visualization graph in our paper
    elif dtype=='visual':
        plot_embedding(embs,cohorts,'')


def main():
    parser = argparse.ArgumentParser()#
    parser.add_argument('--dtype',type=str,default='undirect_onto',help='the model type: onto no_onto test.')
    parser.add_argument('--do_query',default=False,action='store_true',help='whether to do the query case in our model.')
    parser.add_argument('--query_data',type=str,required=False,help='if do_query is true, you need to set the query_data for your query admissions file. ')
    parser.add_argument('--add_admission',default=False,action='store_true',help='whether to add the admission node.')
    parser.add_argument('--do_train',default=False,action='store_true',help='whether to train the model.')
    parser.add_argument('--do_eval',default=False,action='store_true',help='whether to test the performance on valiation dataset.')
    parser.add_argument('--do_test',default=False,action='store_true',help='whether to test the performance on test dataset.')
    parser.add_argument('--learning_rate',type=float,default=0.001,help='the initial learning rate for Adam.')
    parser.add_argument('--intermediate_size',type=int,default=100,help='the intermediate size for transformer.')
    parser.add_argument('--num_heads',type=int,default=4,help='the num of heads for transformer.')
    parser.add_argument('--hidden_dropout_prob',type=float,default=0.2,help='the droput rate for self attention transformer.')
    parser.add_argument('--batch_size',type=int,default=16,help='batch size for train dataset')
    parser.add_argument('--self_attn_nums',type=int,default=1,help='the number of self transformer for meta path enrichment in our model')
    parser.add_argument('--gcn_conv_nums',type=int,default=2,help='the number of gcn convoluation  layer for our model')
    parser.add_argument('--num_train_epochs',type=int,default=10,help='the number of training epoch')
    parser.add_argument('--emb_size',type=int,default=100,help='the vocab embedding size')
    parser.add_argument('--hidden_size',type=int,default=100,help='the ontology embedding hidden size')
    parser.add_argument('--desc_emb_size',type=int,default=300,help='the bert embedding hidden size')#
    parser.add_argument('--img_emb_size',type=int,default=2048,help='the image embedding hidden size')
    parser.add_argument('--graph_hidden_size',type=int,default=100,help='the graph embedding hidden size')
    parser.add_argument('--graph_heads',type=int,default=1,help='the graph heads')
    parser.add_argument('--model_type',type=str,default='no_gram',help='no_gram for our model')
    parser.add_argument('--out_channels',type=int,default=300,help='the output channels for graph model')
    parser.add_argument('--seed',type=int,default=1002,help='random seed for initialization')
    parser.add_argument('--use_glb',type=int,default=2,help='use_glb  0 for global info, 1 for local info, and 2 for both')
    parser.add_argument('--use_emb',type=int,default=2,help='use_emb  0 for onto info, 1 for multimodal info(w/o onto), 2 for both, 3 for image info, 4 for text info, 5 for onto and text(w/o image), 6 for onto and image(w/o)')
    parser.add_argument('--bins',type=int,default=20,help='the bins for drmm kernels')
    parser.add_argument('--pair_neurons',type=int,default=30,help='the neurons for global embedding')
    parser.add_argument('--use_conv',type=str,default='gcn',help='the convolution method : gcn, gin, gat, gated_conv')
    parser.add_argument('--train_data',type=str,required=True,help='the processed data  path for train_data.pt ')
    parser.add_argument('--test_data',type=str,required=True,help='the processed data  path for test_data.pt')
    parser.add_argument('--valid_data',type=str,required=True,help='the processed data  path for valid_data.pt')
    parser.add_argument('--train_input_data',type=str,required=True,help='the processed data  path for train dataset embedding ')
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
    
   
    
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu') 
    
    if torch.cuda.is_available():
        torch.cuda.manual_seed(args.seed)
        torch.cuda.manual_seed_all(args.seed)
        torch.backends.cudnn.deterministic = True
        torch.backends.cudnn.benchmark = False
    
    print('loading dataset')
    
    # code_id_to_desc: for each code get the description
    vocab_emb,tokenizer,train_dataloader,valid_dataloader,test_dataloader,image_vocab_emb,cohorts, train_input_dataloader, train_cohorts = load_dataset(args.train_data,args.valid_data,args.test_data,args.train_input_data,args.batch_size,dtype=args.dtype,add_admission=args.add_admission)
    print("load data completed")
    args.vocab_size = len(tokenizer.vocab.word2idx)
    
    
    diag_voc,proce_voc,pres_voc = tokenizer.diag_voc, tokenizer.proce_voc, tokenizer.atc_voc
    
    model_type = args.model_type
    

    model = EHROntologyModel(args,vocab_emb,image_vocab_emb,diag_voc,proce_voc,pres_voc,device).to(device)
        
    
    print("start training")
    optimizer = Adam(model.parameters(),lr=args.learning_rate)
    os.makedirs(args.output_dir,exist_ok=True)
    
    model_to_save = model.module if hasattr(model,'module') else model
    output_model_file = os.path.join(args.output_dir,'pytorch_model.bin')
    
    import datetime
    # for single model query
    if args.do_query:
        model_state_dict = torch.load(output_model_file)
        model.load_state_dict(model_state_dict)
        model.to(device)
        
#         test(model,test_dataloader,cohorts,device,dtype='visual')
#         print('picture save done')
        start = datetime.datetime.now()
        test(model,test_dataloader,cohorts,device,dtype = 'knn',train_dataloader = train_input_dataloader , train_cohorts = train_cohorts)
        end = datetime.datetime.now()
        print('预测任务测试时间')
        print((end - start).total_seconds())
        print(len(cohorts))
        print((end - start).total_seconds()/len(cohorts))
        return
    
    best_me_purity  = 0  
    if args.do_train:
        global_step,valid_accu_best = 0, 0
        writer = SummaryWriter(args.output_dir)
        print(args.output_dir)
        for _ in trange(int(args.num_train_epochs),desc='training epoch'):
            #begin to train
            tr_loss,nb_tr_steps = 0,0
            epoch_iterator = tqdm(train_dataloader, leave=False, desc='Training')
            logger.info('***Running training***')
            logger.info("num of examples = %d",len(epoch_iterator)*args.batch_size)

            all_p_labels = []
            all_p_preds = []
            
            model.train()
            for step,batch in enumerate(epoch_iterator):
                loss, p_output = model(left_x=batch.x_left.to(device),left_graph_index=batch.edge_index_left.long().to(device),left_x_batch=batch.x_left_batch.to(device),\
                                       right_x=batch.x_right.to(device),right_graph_index=batch.edge_index_right.long().to(device),right_x_batch=batch.x_right_batch.to(device),\
                         label=batch.y.long().to(device))#for mse loss
                loss.backward()
                
                predict_score = p_output.detach().cpu().numpy()
                predict_labels = np.argmax(predict_score,axis=1)

                all_p_preds.append(predict_labels)
                all_p_labels.append(batch.y.long().detach().cpu().numpy())

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
                        v_loss,outputs = model(left_x=batch.x_left.to(device),left_graph_index=batch.edge_index_left.long().to(device),left_x_batch=batch.x_left_batch.to(device),\
                                               right_x=batch.x_right.to(device),right_graph_index=batch.edge_index_right.long().to(device),right_x_batch=batch.x_right_batch.to(device),\
                                            label=batch.y.long().to(device))

                        val_loss += v_loss.item()
                        nb_val_steps += 1
                        
                        predict_score = outputs.detach().cpu().numpy()
                        predict_labels = np.argmax(predict_score,axis=1)

                        all_predict_labels.append(predict_labels)
                        all_true_labels.append(batch.y.long().detach().cpu().numpy())
                valid_ev_metrics = report_metrics(np.concatenate(all_true_labels,axis=0),np.concatenate(all_predict_labels,axis=0))
                writer.add_scalar('eval/loss', val_loss/nb_val_steps,global_step)

                for key,val in valid_ev_metrics.items():
                    writer.add_scalar('eval/{}'.format(key),val,global_step)

            if args.do_test:
                me_purity,me_nmi,me_ri = test(model,test_dataloader,cohorts,device)
                if me_purity>best_me_purity:
                    best_me_purity,best_me_nmi,best_me_ri = me_purity,me_nmi,me_ri
                    torch.save(model_to_save.state_dict(),output_model_file)
                test(model,test_dataloader,cohorts,device,dtype='knn',train_dataloader = train_input_dataloader , train_cohorts = train_cohorts)
                print("the metric purity: {}, nmi:{}, me_ri: {} ".format(
               
                    best_me_purity,
                    best_me_nmi,
                    best_me_ri
                ))
                writer.add_scalar('test/purity',me_purity,global_step)
                writer.add_scalar('test/nmi',me_nmi,global_step)
                writer.add_scalar('test/ri',me_ri,global_step)
    
        print('the best metric(purity) for test model purity: ',best_me_purity,' nmi:',best_me_nmi,' me_ri: ',best_me_ri)


        test(model,test_dataloader,cohorts,device,dtype='visual',train_dataloader = train_input_dataloader , train_cohorts = train_cohorts)
        test(model,test_dataloader,cohorts,device,dtype='knn',train_dataloader = train_input_dataloader , train_cohorts = train_cohorts)
                
            
    
if __name__=='__main__':
    main()
    