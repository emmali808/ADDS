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

    
