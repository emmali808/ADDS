from pytools import memoize_method
import torch
import torch.nn.functional as F
import modeling_util

import json
        

class BaseRanker(torch.nn.Module):
    def __init__(self,config_path):
        super().__init__()
        self.config = json.load(open(config_path, 'r'))
        vocab_embed = modeling_util.get_vocab(self.config)
        self.embed = torch.nn.Embedding.from_pretrained(vocab_embed)
        
    def forward(self, **inputs):
        raise NotImplementedError
        
    def save(self, path):
        state = self.state_dict(keep_vars=True)
        for key in list(state):
            if state[key].requires_grad:
                state[key] = state[key].data
            else:
                del state[key]
        torch.save(state, path)

    def load(self, path):
        self.load_state_dict(torch.load(path), strict=False)
        
    def encode(self, query_tok, doc_tok):
        return self.embed(query_tok+1),self.embed(doc_tok+1)
        
class BaseTransformRankder(torch.nn.Module):
    def __init__(self,config_path):
        super().__init__()

        self.config = json.load(open(config_path, 'r'))
        vocab_embed = modeling_util.get_kg_vocab(self.config)
        self.embed = torch.nn.Embedding.from_pretrained(vocab_embed)

        self.WORD_SIZE = self.config['word_dim']
        self.ENTITY_SIZE = self.config['entity_dim']
        self.ATTEN_SIZE = self.config['atten_dim']

        self.transform = torch.nn.Linear(self.WORD_SIZE,self.ATTEN_SIZE)
        self.transform_ent = torch.nn.Linear(self.ENTITY_SIZE,self.ATTEN_SIZE)

    def forward(self, **inputs):
        raise NotImplementedError

    def save(self, path):
        state = self.state_dict(keep_vars=True)
        for key in list(state):
            if state[key].requires_grad:
                state[key] = state[key].data
            else:
                del state[key]
        torch.save(state, path)

    def load(self, path):
        self.load_state_dict(torch.load(path), strict=False)

    def encode(self, query_tok, doc_tok):
        return self.embed(query_tok + 1), self.embed(doc_tok + 1)

    def transform_embed(self,query_reps,doc_reps,query_entity,doc_entity):

        query_embed = self.transform(query_reps)
        doc_embed = self.transform(doc_reps)

        query_entity = self.transform_ent(query_entity)#batch*q_len*atten_size
        doc_entity = self.transform_ent(doc_entity)

        query_trans_reps =  torch.relu(query_embed+query_entity)#batch*q_len*dim
        doc_trans_reps =  torch.relu(doc_embed+doc_entity)

        return query_trans_reps,doc_trans_reps



class PacrrRanker(BaseRanker):
    def __init__(self,config_path):
        super().__init__(config_path)
        #QLEN = 10
        KMAX = 2
        NFILTERS = 32
        MINGRAM = 1
        MAXGRAM = 3
        self.simmat = modeling_util.SimmatModule()
        self.ngrams = torch.nn.ModuleList()
        self.rbf_bank = None
        self.CHANNELS = 1
        for ng in range(MINGRAM, MAXGRAM+1):
            ng = modeling_util.PACRRConvMax2dModule(ng, NFILTERS, k=KMAX, channels=self.CHANNELS)
            self.ngrams.append(ng)
        qvalue_size = len(self.ngrams) * KMAX
        #self.linear1 = torch.nn.Linear(self.BERT_SIZE + QLEN * qvalue_size, 32)
        self.linear1 = torch.nn.Linear(self.config['text1_maxlen'] * qvalue_size, 32)
        self.linear2 = torch.nn.Linear(32, 32)
        self.linear3 = torch.nn.Linear(32, 1)

    def forward(self, query_tok, doc_tok):
        query_reps, doc_reps = self.encode(query_tok,doc_tok)

        #query_reps, doc_reps = self.transform_embed(query_reps,doc_reps,query_entity,doc_entity)#batch*q_len*200
        simmat = self.simmat(query_reps, doc_reps, query_tok, doc_tok)
        scores = [ng(simmat) for ng in self.ngrams]
        scores = torch.cat(scores, dim=2)
        scores = scores.reshape(scores.shape[0], scores.shape[1] * scores.shape[2])
        #scores = torch.cat([scores, cls_reps[-1]], dim=1)
        rel = F.relu(self.linear1(scores))
        rel = F.relu(self.linear2(rel))
        rel = self.linear3(rel)
        return rel


class DrmmTKSRanker(BaseRanker):
    def __init__(self, config_path):
        super().__init__(config_path)

        # self.bert_ranker = VanillaBertRanker()
        self.topk = 20
        self.BERT_SIZE = 300
        # self.ENTITY_SIZE = 100
        # self.ATTEN_SIZE = 200

        self.attention = modeling_util.Attention(self.BERT_SIZE)  # combine+transform
        # self.transform = torch.nn.Linear(self.BERT_SIZE,self.ATTEN_SIZE)#combine+transform
        # self.transform_ent = torch.nn.Linear(self.ENTITY_SIZE,self.ATTEN_SIZE)#combine+transform
        self.linear = torch.nn.Linear(self.topk, 1)
        self.out = torch.nn.Linear(1, 1)
        # self.out = torch.nn.Linear(self.BERT_SIZE+13,1)# combine+transform

    def forward(self, query_tok, doc_tok):
        query_reps, doc_reps = self.encode(query_tok, doc_tok)

        # query_reps, doc_reps = self.transform_embed(query_reps,doc_reps,query_entity,doc_entity)#batch*q_len*200

        matching_matrix = torch.einsum('bld,brd->blr', F.normalize(query_reps, p=2, dim=-1),
                                       F.normalize(doc_reps, p=2, dim=-1))  # batch*q_len*d_len

        matching_top_k = torch.topk(matching_matrix, k=self.topk, dim=-1, sorted=True)[0]  # batch*q_len*topk
        attention_probs = self.attention(query_reps)  # batch*q_len

        dense_output = self.linear(matching_top_k).squeeze(dim=-1)  # batch*q_len

        embed_flat = torch.einsum('bl,bl->b', dense_output, attention_probs).unsqueeze(dim=-1)  # batch*1

        res = self.out(embed_flat)
        return res

    
class KnrmRanker(BaseRanker):
    def __init__(self,config_path):
        super().__init__(config_path)
        
        #MUS,SIGMAS = modeling_util.init_kernels(21)
        
        MUS = [-0.9, -0.7, -0.5, -0.3, -0.1, 0.1, 0.3, 0.5, 0.7, 0.9, 1.0]
        SIGMAS = [0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.001]
        
        self.simmat = modeling_util.SimmatModule()
        self.kernels = modeling_util.KNRMRbfKernelBank(MUS, SIGMAS)
        self.combine = torch.nn.Linear(self.kernels.count() , 1)#

    def forward(self, query_tok, doc_tok):
        query_reps, doc_reps = self.encode(query_tok,doc_tok)

        #query_reps, doc_reps = self.transform_embed(query_reps,doc_reps,query_entity,doc_entity)#batch*q_len*200 
        
        
        simmat = self.simmat(query_reps, doc_reps, query_tok, doc_tok)
        kernels = self.kernels(simmat)
        BATCH, KERNELS, VIEWS, QLEN, DLEN = kernels.shape
        kernels = kernels.reshape(BATCH, KERNELS * VIEWS, QLEN, DLEN)
        simmat = simmat.reshape(BATCH, 1, VIEWS, QLEN, DLEN) \
                       .expand(BATCH, KERNELS, VIEWS, QLEN, DLEN) \
                       .reshape(BATCH, KERNELS * VIEWS, QLEN, DLEN)
        result = kernels.sum(dim=3) # sum over document
        mask = (simmat.sum(dim=3) != 0.) # which query terms are not padding?
        result = torch.where(mask, (result + 1e-6).log(), mask.float())
        result = result.sum(dim=2) # sum over query terms
        #result = torch.cat([result, cls_reps[-1]], dim=1)#
        scores = self.combine(result) # linear combination over kernels
        return scores    

    
class PacrrKGRanker(BaseTransformRankder):
    def __init__(self,config_path):
        super().__init__(config_path)
        #QLEN = 10
        KMAX = 2
        NFILTERS = 32
        MINGRAM = 1
        MAXGRAM = 3
        self.simmat = modeling_util.SimmatModule()
        self.ngrams = torch.nn.ModuleList()
        self.rbf_bank = None
        self.CHANNELS = 1
        for ng in range(MINGRAM, MAXGRAM+1):
            ng = modeling_util.PACRRConvMax2dModule(ng, NFILTERS, k=KMAX, channels=self.CHANNELS)
            self.ngrams.append(ng)
        qvalue_size = len(self.ngrams) * KMAX
        #self.linear1 = torch.nn.Linear(self.BERT_SIZE + QLEN * qvalue_size, 32)
        self.linear1 = torch.nn.Linear(self.config['text1_maxlen'] * qvalue_size, 32)
        self.linear2 = torch.nn.Linear(32, 32)
        self.linear3 = torch.nn.Linear(32, 1)

    def forward(self, query_tok, doc_tok,query_entity,doc_entity):
        query_reps, doc_reps = self.encode(query_tok,doc_tok)

        query_reps, doc_reps = self.transform_embed(query_reps,doc_reps,query_entity,doc_entity)#batch*q_len*200
        simmat = self.simmat(query_reps, doc_reps, query_tok, doc_tok)
        scores = [ng(simmat) for ng in self.ngrams]
        scores = torch.cat(scores, dim=2)
        scores = scores.reshape(scores.shape[0], scores.shape[1] * scores.shape[2])
        #scores = torch.cat([scores, cls_reps[-1]], dim=1)
        rel = F.relu(self.linear1(scores))
        rel = F.relu(self.linear2(rel))
        rel = self.linear3(rel)
        return rel

class KnrmKGRanker(BaseTransformRankder):
    def __init__(self, config_path):
        super().__init__(config_path)

        # MUS,SIGMAS = modeling_util.init_kernels(21)

        MUS = [-0.9, -0.7, -0.5, -0.3, -0.1, 0.1, 0.3, 0.5, 0.7, 0.9, 1.0]
        SIGMAS = [0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.001]

        self.simmat = modeling_util.SimmatModule()
        self.kernels = modeling_util.KNRMRbfKernelBank(MUS, SIGMAS)
        self.combine = torch.nn.Linear(self.kernels.count(), 1)  #

    def forward(self, query_tok, doc_tok,query_entity,doc_entity):
        query_reps, doc_reps = self.encode(query_tok, doc_tok)

        query_reps, doc_reps = self.transform_embed(query_reps,doc_reps,query_entity,doc_entity)#batch*q_len*200

        simmat = self.simmat(query_reps, doc_reps, query_tok, doc_tok)
        kernels = self.kernels(simmat)
        BATCH, KERNELS, VIEWS, QLEN, DLEN = kernels.shape
        kernels = kernels.reshape(BATCH, KERNELS * VIEWS, QLEN, DLEN)
        simmat = simmat.reshape(BATCH, 1, VIEWS, QLEN, DLEN) \
            .expand(BATCH, KERNELS, VIEWS, QLEN, DLEN) \
            .reshape(BATCH, KERNELS * VIEWS, QLEN, DLEN)
        result = kernels.sum(dim=3)  # sum over document
        mask = (simmat.sum(dim=3) != 0.)  # which query terms are not padding?
        result = torch.where(mask, (result + 1e-6).log(), mask.float())
        result = result.sum(dim=2)  # sum over query terms
        # result = torch.cat([result, cls_reps[-1]], dim=1)#
        scores = self.combine(result)  # linear combination over kernels
        return scores


class KnrmKGRanker(BaseTransformRankder):
    def __init__(self, config_path):
        super().__init__(config_path)

        # MUS,SIGMAS = modeling_util.init_kernels(21)

        MUS = [-0.9, -0.7, -0.5, -0.3, -0.1, 0.1, 0.3, 0.5, 0.7, 0.9, 1.0]
        SIGMAS = [0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.001]

        self.simmat = modeling_util.SimmatModule()
        self.kernels = modeling_util.KNRMRbfKernelBank(MUS, SIGMAS)
        self.combine = torch.nn.Linear(self.kernels.count(), 1)  #

    def forward(self, query_tok, doc_tok,query_entity,doc_entity):
        query_reps, doc_reps = self.encode(query_tok, doc_tok)

        query_reps, doc_reps = self.transform_embed(query_reps,doc_reps,query_entity,doc_entity)#batch*q_len*200

        simmat = self.simmat(query_reps, doc_reps, query_tok, doc_tok)
        kernels = self.kernels(simmat)
        BATCH, KERNELS, VIEWS, QLEN, DLEN = kernels.shape
        kernels = kernels.reshape(BATCH, KERNELS * VIEWS, QLEN, DLEN)
        simmat = simmat.reshape(BATCH, 1, VIEWS, QLEN, DLEN) \
            .expand(BATCH, KERNELS, VIEWS, QLEN, DLEN) \
            .reshape(BATCH, KERNELS * VIEWS, QLEN, DLEN)
        result = kernels.sum(dim=3)  # sum over document
        mask = (simmat.sum(dim=3) != 0.)  # which query terms are not padding?
        result = torch.where(mask, (result + 1e-6).log(), mask.float())
        result = result.sum(dim=2)  # sum over query terms
        # result = torch.cat([result, cls_reps[-1]], dim=1)#
        scores = self.combine(result)  # linear combination over kernels
        return scores


class DrmmTKSKGRanker(BaseTransformRankder):
    def __init__(self, config_path):
        super().__init__(config_path)

        # self.bert_ranker = VanillaBertRanker()
        self.topk = 20
        self.attention = modeling_util.Attention(self.ATTEN_SIZE)  # combine+transform
        # self.transform = torch.nn.Linear(self.BERT_SIZE,self.ATTEN_SIZE)#combine+transform
        # self.transform_ent = torch.nn.Linear(self.ENTITY_SIZE,self.ATTEN_SIZE)#combine+transform
        self.linear = torch.nn.Linear(self.topk, 1)
        self.out = torch.nn.Linear(1, 1)
        # self.out = torch.nn.Linear(self.BERT_SIZE+13,1)# combine+transform

    def forward(self, query_tok, doc_tok,query_entity,doc_entity, flag=False):
        query_reps, doc_reps = self.encode(query_tok, doc_tok)

        query_reps, doc_reps = self.transform_embed(query_reps,doc_reps,query_entity,doc_entity)#batch*q_len*200

        matching_matrix = torch.einsum('bld,brd->blr', F.normalize(query_reps, p=2, dim=-1),
                                       F.normalize(doc_reps, p=2, dim=-1))  # batch*q_len*d_len

        matching_top_k = torch.topk(matching_matrix, k=self.topk, dim=-1, sorted=True)[0]  # batch*q_len*topk
        attention_probs = self.attention(query_reps)  # batch*q_len

        dense_output = self.linear(matching_top_k).squeeze(dim=-1)  # batch*q_len

        embed_flat = torch.einsum('bl,bl->b', dense_output, attention_probs).unsqueeze(dim=-1)  # batch*1

        res = self.out(embed_flat)
        if flag:
            return res, attention_probs, matching_matrix
        return res

