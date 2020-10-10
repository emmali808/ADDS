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

    
