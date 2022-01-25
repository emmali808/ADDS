import numpy as np
import pandas as pd
import torch
import pickle
from torch_geometric.data import InMemoryDataset,Data,DataLoader
from tqdm import tqdm
import os
from build_tree import Voc
from sklearn.metrics import accuracy_score,precision_score,f1_score,recall_score
import numpy as np


edge_nums = 0
node_nums = 0

'''
    self.vocab.word2idx: 'd_'+code 'p_'+code 'a_'+code
'''
class EHRTokenizer(object):
    def __init__(self,vocab_file,word_embeddings_dim=100,add_admission=False):
        self.add_admission = add_admission
        self.vocab = Voc()#fused all codes such as diag_codes,proce_codes and atc_codes
        self.diag_voc,self.proce_voc,self.atc_voc = self.add_vocab(vocab_file) # get for build ontology EHR Model     
#         self.med_embedding = self.init_med_embedding(word_embeddings_dim)
        
    def add_vocab(self,vocab_file):
        voc1,voc2,voc3 = Voc(),Voc(),Voc()
        # the first index map the admission
        if self.add_admission:
            self.vocab.add_sentence(['admission'])
        
        all_codes_dic = pickle.load(open(vocab_file,'rb'))
        diag_codes,proce_codes,atc_codes = ['d_'+d for d in all_codes_dic['diag_codes']], ['p_'+p for p in all_codes_dic['proce_codes']], ['a_'+a for a in all_codes_dic['atc_codes']]
        
        voc1.add_sentence(diag_codes)
        self.vocab.add_sentence(diag_codes)
        
        voc2.add_sentence(proce_codes)
        self.vocab.add_sentence(proce_codes)
        
        voc3.add_sentence(atc_codes)
        self.vocab.add_sentence(atc_codes)
        
        if self.add_admission:
            assert len(self.vocab.word2idx) == len(voc1.word2idx)+len(voc2.word2idx)+len(voc3.word2idx)+1
        else:
            assert len(self.vocab.word2idx) == len(voc1.word2idx)+len(voc2.word2idx)+len(voc3.word2idx)
        
        return voc1,voc2,voc3

    
    # for each single graph transform the code to index
    def build_single_graph(self,diag_codes,proce_codes,atc_codes):
        single_voc = Voc()
        if self.add_admission:
            single_voc.add_sentence(['admission'])
        single_voc.add_sentence(['d_'+d for d in diag_codes])
        single_voc.add_sentence(['p_'+p for p in proce_codes])
        single_voc.add_sentence(['a_'+a for a in atc_codes])
        
        sorted_idx = sorted(single_voc.idx2word.keys())
        sorted_codes = []
        for idx in sorted_idx:
            code = single_voc.idx2word[idx]
            sorted_codes.append(code)
        
        return single_voc , self.convert_codes_to_ids(sorted_codes,'')
    
    # construct the graph with ontology code index
    def build_onto_single_graph(self,diag_codes,proce_codes,atc_codes):
        single_voc = Voc()#build for the patient graph
        if self.add_admission:
            single_voc.add_sentence(['admission'])
        single_voc.add_sentence(['d_'+d for d in diag_codes])
        single_voc.add_sentence(['p_'+p for p in proce_codes])
        single_voc.add_sentence(['a_'+a for a in atc_codes])
        
        anum = 0
        pnum = 0
        dnum = 0
        #map the code with ontology embedding index, get as the feature x
        if self.add_admission:
            code_ids = [self.vocab.word2idx['admission']]
        else:
            code_ids = []
#         print(self.vocab.word2idx)
        for code in diag_codes:
            cc  = 'd_'+code
            if self.vocab.word2idx.get(cc, -1) == -1:
                dnum = dnum + 1
                ff = open("../mimic-iv/no_exist/diag_no_exist.txt", "a")
                ff.write(code+'\n')
            else:
                code_ids.append(self.vocab.word2idx['d_'+code])
        for code in proce_codes:
            cc = 'p_'+code
            if self.vocab.word2idx.get(cc, -1) == -1:
                pnum = pnum + 1
                ff = open("../mimic-iv/no_exist/proce_no_exist.txt", "a")
                ff.write(code+'\n')
            else:
                code_ids.append(self.vocab.word2idx['p_'+code])
        for code in atc_codes:
            cc = 'a_'+code
            if self.vocab.word2idx.get(cc, -1) == -1:
                anum = anum + 1
                ff = open("../mimic-iv/no_exist/atc_no_exist.txt", "a")
                ff.write(code+'\n')
            else:
                code_ids.append(self.vocab.word2idx['a_'+code])
        
        return single_voc,code_ids,len(diag_codes)
    
    
#     def init_med_embedding(self,word_embeddings_dim=100):
       
#         med_nums = len(self.vocab.word2idx)
#         return np.random.uniform(-0.01, 0.01,
#                                  (med_nums, word_embeddings_dim))
    
#     def convert_ids_to_embeddings(self,ids):
#         return self.med_embedding[ids,:]

    
    def convert_codes_to_ids(self,codes,c_type):
        ids = []
        for code in codes:
            ids.append(self.vocab.word2idx[c_type+code])
        return ids


    
class UndirectPatientOntoGraphEx(object):
    def __init__(self,diag_codes,proce_codes,atc_codes,rel_infos,tokenizer,p_a_include = True):
        self.diag_codes = diag_codes
        self.proce_codes = proce_codes
        self.atc_codes = atc_codes
        self.p_a_include = p_a_include
        
        self.tokenizer = tokenizer
        
        self.diag_proce_rel,self.diag_atc_rel,self.proce_atc_rel = rel_infos
        
        self.x, self.edge_index, self.diag_cnt = self.build_patient_graph(self.tokenizer,diag_codes,proce_codes,atc_codes)
        

    #construct patient graph onto_code_ids for ontology index mapping
    def build_patient_graph(self,tokenizer,diag_codes,proce_codes,atc_codes):
        single_voc,onto_code_ids,diag_cnt = tokenizer.build_onto_single_graph(diag_codes,proce_codes,atc_codes)
        #if there has the relations, construct the edge in this graph
        edge_idx = []
        global edge_nums
        
        if tokenizer.add_admission:
            [edge_idx.extend([(single_voc.word2idx['d_'+d],0),(0,single_voc.word2idx['d_'+d])]) for d in diag_codes]
            [edge_idx.extend([(single_voc.word2idx['p_'+p],0),(0,single_voc.word2idx['p_'+p])]) for p in proce_codes]
            [edge_idx.extend([(single_voc.word2idx['a_'+a],0),(0,single_voc.word2idx['a_'+a])]) for a in atc_codes]
        
        all_diag_proce_pairs = [(d,p) for d in diag_codes for p in proce_codes]

        
        
        # valid_diag_proce_pairs = [edge_idx.extend([(single_voc.word2idx['d_'+d_p[0]],single_voc.word2idx['p_'+d_p[1]]),(single_voc.word2idx['p_'+d_p[1]],single_voc.word2idx['d_'+d_p[0]])]) for d_p in all_diag_proce_pairs if d_p[0]+'-'+d_p[1] in self.diag_proce_rel ]
#         edge_idx.extend(valid_diag_proce_pairs)
        # edge_nums = edge_nums + len(valid_diag_proce_pairs)
        
        
        all_diag_atc_pairs = [(d,a) for d in diag_codes for a in atc_codes]
        
        valid_diag_atc_pairs = [edge_idx.extend([(single_voc.word2idx['d_'+d_a[0]],single_voc.word2idx['a_'+d_a[1]]),(single_voc.word2idx['a_'+d_a[1]],single_voc.word2idx['d_'+d_a[0]])]) for d_a in all_diag_atc_pairs if d_a[0]+'-'+d_a[1] in self.diag_atc_rel ]
        # valid_diag_atc_pairs = [edge_idx.extend([(single_voc.word2idx['d_'+d_a[0]],single_voc.word2idx['a_'+d_a[1]]),(single_voc.word2idx['a_'+d_a[1]],single_voc.word2idx['d_'+d_a[0]])]) for d_a in all_diag_atc_pairs if d_a[0]+'-'+d_a[1] in self.diag_atc_rel ]
#         edge_idx.extend(valid_diag_atc_pairs)
        edge_nums = edge_nums + len(valid_diag_atc_pairs)
          
        if self.p_a_include:
            
            all_proce_atc_pairs = [(p,a) for p in proce_codes for a in atc_codes]
            # edge_nums = edge_nums + len(all_proce_atc_pairs)
            valid_proce_atc_pairs = [edge_idx.extend([(single_voc.word2idx['p_'+p_a[0]],single_voc.word2idx['a_'+p_a[1]]),(single_voc.word2idx['a_'+p_a[1]],single_voc.word2idx['p_'+p_a[0]])]) for p_a in all_proce_atc_pairs if p_a[0]+'-'+p_a[1] in self.proce_atc_rel ]
            edge_nums = edge_nums + len(valid_proce_atc_pairs)
#         edge_idx.extend(valid_proce_atc_pairs)

        row = list(map(lambda x: x[0], edge_idx))
        col = list(map(lambda x: x[1], edge_idx))
        # print(edge_idx)
        # if len(row)==0:
            # print('empty graph')
        
        global node_nums
        node_nums = node_nums + len(onto_code_ids)
        edge_nums = edge_nums + len(edge_idx)
        # print(single_voc.word2idx)
        
        
        return  onto_code_ids , [row,col], diag_cnt

'''
    construct all the kg relations from the kg relations file
'''
def load_rel():
    pmi_threshold = 1
    base_dir = '/home/lf/桌面/MIMIC全/mimic_tester/'
    
    rel_dir = os.path.join(base_dir,'../self_kg/')
    diag_proce_df = pd.read_csv(rel_dir+'diag_proce_rel.csv',sep='\t')
    diag_proce_df = diag_proce_df[diag_proce_df['pmi']>pmi_threshold]
    diag_pres_df = pd.read_csv(rel_dir+'diag_pres_rel.csv',sep='\t',dtype={'tail ent':str})
    diag_pres_df = diag_pres_df[diag_pres_df['pmi']>pmi_threshold]
    proce_pres_df = pd.read_csv(rel_dir+'proce_pres_rel.csv',sep='\t',dtype={'tail ent':str})
    proce_pres_df = proce_pres_df[proce_pres_df['pmi']>pmi_threshold]

    ndc2rxnorm_file = os.path.join(base_dir,'ndc_atc/ndc2rxnorm_mapping.txt')
    with open(ndc2rxnorm_file,'r') as f:
        ndc2rxnorm = eval(f.read())

    diag_proce_pairs = []
    diag_proce_df.apply(lambda row:diag_proce_pairs.append(str(row['head ent'])+'-'+str(row['tail ent'])),axis=1)

    diag_pres_pairs = []
    diag_pres_df.apply(lambda row:diag_pres_pairs.append(str(row['head ent'])+'-'+ndc2rxnorm[row['tail ent']]),axis=1)

    proce_pres_pairs = []
    proce_pres_df.apply(lambda row:proce_pres_pairs.append(str(row['head ent'])+'-'+ndc2rxnorm[row['tail ent']]),axis=1)
    
    return diag_proce_pairs,diag_pres_pairs,proce_pres_pairs
                     

# loading the relationship from our knowledge graph
REL_INFOS = load_rel()

# pair graph similarity learning
# y as the label
class EHRPairData(Data):
    def __init__(self,x_left,edge_index_left,x_right,edge_index_right,left_diag_cnt,right_diag_cnt,y):
        super(EHRPairData,self).__init__()
        self.x_left = x_left
        self.edge_index_left = edge_index_left
        self.x_right = x_right
        self.edge_index_right = edge_index_right
        self.left_diag_cnt = left_diag_cnt
        self.right_diag_cnt = right_diag_cnt
        self.y = y
        
    def __inc__(self,key,value):
        if key=='edge_index_left':
            return  self.x_left.size(0)
        if key=='edge_index_right':
            return self.x_right.size(0)
        else:
            return super(EHRPairData,self).__inc__(key,value)
        
def load_ehr_infos(ehr_file,tokenizer,dtype='undirect_onto'):
    ehr_infos = {}
    ehr_df = pd.read_csv(ehr_file)
    empty_graph_nums = 0
    global node_nums
    global edge_nums

    edge_nums = 0
    node_nums = 0
    ddid = ""
    ff = False
    for idx,row in ehr_df.iterrows():
        hadm_id = row['HADM_ID']
        if ff == False:
            ff = True
            ddid = hadm_id
        diag_codes = list(set(row['ICD9_DIAG'].split(',')))
        proce_codes = list(set(row['ICD9_PROCE'].split(',')))
        atc_codes = list(set(row['ATC'].split(',')))
        
        if dtype=='pa_onto':
            ehr_graph = UndirectPatientOntoGraphEx(diag_codes,proce_codes,atc_codes,REL_INFOS,tokenizer,p_a_include=True)
        elif dtype=='undirect_onto':
            ehr_graph = UndirectPatientOntoGraphEx(diag_codes,proce_codes,atc_codes,REL_INFOS,tokenizer,p_a_include=False)

        if len(ehr_graph.edge_index[0])==0:
            empty_graph_nums += 1
            fe = open("../mimic-iv/empty_g_a.txt", "a")
            fe.write(str(row['HADM_ID'])+'\n')

        ehr_infos[hadm_id] = ehr_graph

    
    print(ehr_file,'construct', node_nums, 'nodes')
    print(ehr_file,'construct', edge_nums, 'edges')
    print(ehr_file,' construct ',empty_graph_nums,' empty graphs.')
    
    return ehr_infos

def load_ehr_pairs(label_file):
    ehr_pairs = []
    label_df = pd.read_csv(label_file,sep='\t')
    for idx,row in label_df.iterrows():
        ehr_pairs.append([row[0],row[1],int(row[2])])
    return ehr_pairs

def construct_query_pairs(ehr_file):
    ehr_df = pd.read_csv(ehr_file)
    left_ehr_id = ehr_df.loc[0,'HADM_ID']
    right_ehr_ids = list(ehr_df.loc[1:,'HADM_ID'].values)

    return [left_ehr_id]*len(right_ehr_ids),right_ehr_ids

def get_cohorts(test_ehr_file, train_ehr_file):
    train_df, test_df = pd.read_csv(train_ehr_file), pd.read_csv(test_ehr_file)
    diseases = list(set(train_df['disease'].values))
    cohorts, train_cohorts = [], []
    for idx, row in train_df.iterrows():
        train_cohorts.append(diseases.index(row['disease']))
    for idx, row in test_df.iterrows():
        cohorts.append(diseases.index(row['disease']))
    return cohorts, train_cohorts

def construct_query_pairs_aff(ehr_file):
    test_admission_df = pd.read_csv(ehr_file)
    
    test_hadm_ids = test_admission_df['HADM_ID'].values
    diseases = list(set(test_admission_df['disease'].values))
    
    test_diseases = []
    for idx, row in test_admission_df.iterrows():
        test_diseases.append(diseases.index(row['disease']))
    
    assert len(test_hadm_ids) == len(test_diseases)
    return test_hadm_ids, test_diseases


#do_cluster部分加入了单个batch data
def construct_query_dataloder(tokenizer,processed_file,ehr_file,batch_size=1,dtype='no_onto',oper_type='normal'):
    if oper_type == 'cluster_patient':
        test_hadm_ids, test_diseases = construct_query_pairs_aff(ehr_file)   #, aff_mat, cohorts
        
        if not os.path.exists(processed_file):
            ehr_infos = load_ehr_infos(ehr_file,tokenizer,dtype=dtype)
            data_list, cohorts = [], []
            for i in range(len(test_hadm_ids)):
                left_ehr = ehr_infos[test_hadm_ids[i]] 
                if len(left_ehr.edge_index[0])==0:continue
                left_ehr_x = torch.tensor(left_ehr.x,dtype=torch.long).unsqueeze(1)
                left_ehr_edge_index = torch.tensor(left_ehr.edge_index,dtype=torch.long) 
                cur_idx_data = Data(x=left_ehr_x,edge_index=left_ehr_edge_index)
                
                data_list.append(cur_idx_data)                                
                cohorts.append(test_diseases[i])

            torch.save((cohorts,data_list),processed_file)
        else:
            cohorts, data_list = torch.load(processed_file)
        loader = DataLoader(data_list,batch_size=batch_size,shuffle=False)

        print(len(data_list))
        # print(data_list)

    else:
        left_ids,right_ids = construct_query_pairs(ehr_file)
    
        if not os.path.exists(processed_file):
            ehr_infos = load_ehr_infos(ehr_file,tokenizer,dtype=dtype)
            data_list = []
            for left_id,right_id in zip(left_ids,right_ids):
                left_ehr,right_ehr = ehr_infos[left_id], ehr_infos[right_id]

                if len(left_ehr.edge_index[0])==0:continue
                if len(right_ehr.edge_index[0])==0:continue

                left_diag_cnt = torch.tensor(left_ehr.diag_cnt,dtype=torch.long)
                right_diag_cnt = torch.tensor(right_ehr.diag_cnt,dtype=torch.long)

                left_ehr_x = torch.tensor(left_ehr.x,dtype=torch.long).unsqueeze(1)
                right_ehr_x = torch.tensor(right_ehr.x,dtype=torch.long).unsqueeze(1)

                left_ehr_edge_index = torch.tensor(left_ehr.edge_index,dtype=torch.long)
                right_ehr_edge_index = torch.tensor(right_ehr.edge_index,dtype=torch.long)

                cur_idx_data = EHRPairData(left_ehr_x,left_ehr_edge_index,right_ehr_x,right_ehr_edge_index,left_diag_cnt,right_diag_cnt,torch.tensor(1,dtype=torch.float))# the label is meaningless
                data_list.append(cur_idx_data)
            torch.save((data_list,right_ids),processed_file)
        else:
            data_list,right_ids = torch.load(processed_file)

        loader = DataLoader(data_list,batch_size=batch_size,shuffle=False,follow_batch=['x_left','x_right'])
    
    if oper_type == 'cluster_patient':
        ## 有修改
        return loader,cohorts
        ## return loader,test_hadm_ids,cohorts
    else:
        return loader,right_ids,left_ids[0]
        

def construct_dataloader(processed_file,tokenizer,ehr_file,label_file,shuffle=True,batch_size=1,dtype='undirect_onto'):
    if not os.path.exists(processed_file):
        ehr_pairs = load_ehr_pairs(label_file) 
        ehr_infos = load_ehr_infos(ehr_file,tokenizer,dtype=dtype)
        data_list = []
        
       

        for index in tqdm(range(len(ehr_pairs)),  desc='construct ehr pairs'):
            left_hadm_id, right_hadm_id,label = ehr_pairs[index][0], ehr_pairs[index][1],ehr_pairs[index][2]
            
            # if ehr_infos.get(left_hadm_id, -1) == -1: continue
            # if ehr_infos.get(right_hadm_id, -1) == -1: continue
            if len(ehr_infos[left_hadm_id].edge_index[0])==0:continue
            if len(ehr_infos[right_hadm_id].edge_index[0])==0:continue
            
#             if dtype=='no_onto':
#                 left_ehr_x = torch.tensor(ehr_infos[left_hadm_id].x,dtype=torch.float)
#                 right_ehr_x = torch.tensor(ehr_infos[right_hadm_id].x,dtype=torch.float)
#             else:
            left_diag_cnt = torch.tensor(ehr_infos[left_hadm_id].diag_cnt,dtype=torch.long)
            right_diag_cnt = torch.tensor(ehr_infos[right_hadm_id].diag_cnt,dtype=torch.long)
            
            left_ehr_x = torch.tensor(ehr_infos[left_hadm_id].x,dtype=torch.long).unsqueeze(1)
            right_ehr_x = torch.tensor(ehr_infos[right_hadm_id].x,dtype=torch.long).unsqueeze(1)
        
            left_ehr_edge_index = torch.tensor(ehr_infos[left_hadm_id].edge_index,dtype=torch.long)
            right_ehr_edge_index = torch.tensor(ehr_infos[right_hadm_id].edge_index,dtype=torch.long)

            cur_idx_data = EHRPairData(left_ehr_x,left_ehr_edge_index,right_ehr_x,right_ehr_edge_index,left_diag_cnt,right_diag_cnt,torch.tensor(label,dtype=torch.float))# for mse loss
            data_list.append(cur_idx_data)    
        torch.save(data_list,processed_file)
    else:
        data_list = torch.load(processed_file)

    # print(len(data_list))
    loader = DataLoader(data_list,batch_size=batch_size,shuffle=shuffle,follow_batch=['x_left','x_right'])
    return loader
    
def report_metrics(true_labels,predict_labels):
    return {'accu':accuracy_score(true_labels,predict_labels),'precision':precision_score(true_labels,predict_labels),'recall':recall_score(true_labels,predict_labels),'f1':f1_score(true_labels,predict_labels)}


# text embedding
def load_codes_embeddings():
    base_dir = '/home/lf/桌面/MIMIC全/mimic_tester/'
    codes_embedding_f = open('./claims_codes_hs_300_new.txt','r',encoding='utf-8')

    ndc2rxnorm_file = base_dir+'ndc_atc/ndc2rxnorm_mapping.txt'
    with open(ndc2rxnorm_file,'r') as f:
        ndc2rxnorm = eval(f.read())#['00186504031']
    diag_embeddings, proce_embeddings, atc_embeddings = {} , {}, {}
    codes_embedding_f.readline()
    for line in codes_embedding_f.readlines():
        item = line.split(" ")[0].split('_')
        if len(item)==2:k_type,k_code = item
        else:continue
        embedding = np.array(line.split(" ")[1:-1])
        if k_type == 'IDX':diag_embeddings[k_code.replace('.','')]=embedding
        elif k_type == 'IPR':proce_embeddings[k_code.replace('.','')]=embedding
        elif k_type == 'N' :atc_embeddings[k_code]=embedding
        # elif k_type == 'N' and k_code in ndc2rxnorm:atc_embeddings[ndc2rxnorm[k_code]]=embedding
    return diag_embeddings,proce_embeddings,atc_embeddings

# image embedding
def load_image_embeddings():
    base_dir = '/home/lf/桌面/MIMIC全/mimic_tester/'
    image_embed_dic = pickle.load(open(base_dir+'../self_dataset/vocab_image_emb.pkl','rb'))
    diag_embeddings, proce_embeddings, atc_embeddings = image_embed_dic['diag_img'], image_embed_dic['proce_img'] , image_embed_dic['pres_img']#{'diag_img':diag_dict,'pres_img':pres_dict,'proce_img':proce_dict}

    ndc2rxnorm_file = base_dir+'ndc_atc/ndc2rxnorm_mapping.txt'
    with open(ndc2rxnorm_file,'r') as f:
        ndc2rxnorm = eval(f.read())#['00186504031']
    
    new_atc_embeddings = {}
    for ndc_code, ndc_emb in atc_embeddings.items():
        new_atc_embeddings[ndc2rxnorm[ndc_code]] = ndc_emb
    
    return diag_embeddings,proce_embeddings,new_atc_embeddings
        
        
    
if __name__=='__main__':    
    vocab_file = '../self_dataset/vocab.pkl'
    tokenizer = EHRTokenizer(vocab_file, add_admission = False)
    pre_pro, data_type = '_pa_include_remove_addm', 'train'
    train_processed = 'all_dataset/'+data_type+pre_pro+'_undirect_onto_data.pt'
    train_ehr_file,train_label_file = '../large_cluster_dataset/'+data_type+'_admissions.csv','../large_cluster_dataset/'+data_type+'_label.csv' 
    print('begin to save dataset loader')
    train_dataloader = construct_dataloader(train_processed,tokenizer,train_ehr_file,train_label_file,dtype='pa_onto')
    print('dataset loader done')
    test_processed_file,test_ehr_file = 'all_dataset/test_pa_include_remove_addm_undirect_onto_data.pt', '../large_cluster_dataset/test_admissions.csv'
    construct_query_dataloder(tokenizer,test_processed_file,test_ehr_file,batch_size = 1,dtype='pa_onto',oper_type='cluster_patient')
    test_processed_file,test_ehr_file = 'all_dataset/train_saved_pa_include_remove_addm_undirect_onto_data.pt', '../large_cluster_dataset/train_admissions.csv'
    construct_query_dataloder(tokenizer,test_processed_file,test_ehr_file,batch_size = 1,dtype='pa_onto',oper_type='cluster_patient')
    
   