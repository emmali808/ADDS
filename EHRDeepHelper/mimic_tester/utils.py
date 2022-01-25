import numpy as np
import os
import sys
import math
import torch
import json
import random
from torch import nn
from torch.utils.data import TensorDataset, Dataset, DataLoader
import argparse
from sklearn.metrics import accuracy_score,precision_score,f1_score,recall_score

from sklearn.metrics import (
    precision_recall_fscore_support, 
    roc_auc_score, 
    average_precision_score, 
    auc,
    roc_curve,
    precision_recall_curve)

class eICUDataset(Dataset):
    def __init__(self, data):
        self.data = data
    def __len__(self):
        return len(self.data)
    def __getitem__(self, idx):
        return self.data[idx]

def priors_collate_fn(batch):
    # now, we want to return a big tensor with (batch_idx, idx_1, idx_2) like in gct code
    new_batch = []
    for i, item in enumerate(batch):
        left_num_indices = item[0].shape[-1]
        left_indices = torch.cat((torch.tensor([i]*left_num_indices).reshape(1,-1), item[0]), axis=0)
        
        right_num_indices = item[2].shape[-1]
        right_indices = torch.cat((torch.tensor([i]*right_num_indices).reshape(1,-1), item[2]), axis=0)
        new_batch.append((left_indices, item[1], right_indices, item[3]))
    left_indices = torch.cat([t[0] for t in new_batch], axis=1)
    left_values = torch.cat([t[1] for t in new_batch], axis=-1)
    right_indices = torch.cat([t[2] for t in new_batch], axis=1)
    right_values = torch.cat([t[3] for t in new_batch], axis=-1)
    return left_indices, left_values, right_indices, right_values

def single_priors_collate_fn(batch):
    # now, we want to return a big tensor with (batch_idx, idx_1, idx_2) like in gct code
    new_batch = []
    for i, item in enumerate(batch):
        left_num_indices = item[0].shape[-1]
        left_indices = torch.cat((torch.tensor([i]*left_num_indices).reshape(1,-1), item[0]), axis=0)
        
        new_batch.append((left_indices, item[1]))
    left_indices = torch.cat([t[0] for t in new_batch], axis=1)
    left_values = torch.cat([t[1] for t in new_batch], axis=-1)
    return left_indices, left_values

    
    
def get_extended_attention_mask(attention_mask):
    if attention_mask.dim() == 2:
        extended_attention_mask = attention_mask[:, None, None, :]
    elif attention_mask.dim() == 3:
        extended_attention_mask = attention_mask[:, None, :, :]
    extended_attention_mask = (1.0 - extended_attention_mask) * -10000.0
    return extended_attention_mask
    
def set_seed(seed):
    random.seed(seed)
    np.random.seed(seed)
    torch.manual_seed(seed)
    torch.cuda.manual_seed_all(seed)


def get_linear_schedule_with_warmup(optimizer, num_warmup_steps, num_training_steps, last_epoch=-1):
    def lr_lambda(current_step):
        if current_step < num_warmup_steps:
            return float(current_step) / float(max(1, num_warmup_steps))
        return max(0.0, float(num_training_steps - current_step) / float(max(1, num_training_steps-num_warmup_steps)))
    return torch.optim.lr_scheduler.LambdaLR(optimizer, lr_lambda, last_epoch)

def nested_concat(tensors, new_tensors, dim=0):
    "Concat the `new_tensors` to `tensors` on `dim`. Works for tensors or nested list/tuples of tensors."
    assert type(tensors) == type(
        new_tensors
    ), f"Expected `tensors` and `new_tensors` to have the same type but found {type(tensors)} and {type(new_tensors)}."
    if isinstance(tensors, (list, tuple)):
        return type(tensors)(nested_concat(t, n, dim) for t, n in zip(tensors, new_tensors))
    return torch.cat((tensors, new_tensors), dim=dim)


def nested_numpify(tensors):
    "Numpify `tensors` (even if it's a nested list/tuple of tensors)."
    if isinstance(tensors, (list, tuple)):
        return type(tensors)(nested_numpify(t) for t in tensors)
    return tensors.cpu().numpy()


def nested_detach(tensors):
    "Detach `tensors` (even if it's a nested list/tuple of tensors)."
    if isinstance(tensors, (list, tuple)):
        return type(tensors)(nested_detach(t) for t in tensors)
    return tensors.detach()


# def prepare_data(data, priors_data, device):
#     features = {}
#     features['dx_ints'] = data[0]
#     features['proc_ints'] = data[1]
#     features['dx_masks'] = data[2]
#     features['proc_masks'] = data[3]
#     features['readmission'] = data[4]
#     features['expired'] = data[5]
#     for k, v in features.items():
#         features[k] = v.to(device)
#     priors = {}
#     priors['indices'] = priors_data[0].to(device)
#     priors['values'] = priors_data[1].to(device)
        
    
#     return features, priors


def prepare_data(data, prior_data, device,dtype='normal'):#left_dx_ints, left_proc_ints, left_dx_masks, left_proc_masks, right_dx_ints, right_proc_ints, right_dx_masks, right_proc_masks, all_labels
    left_features = {}
    left_features['dx_ints'] = data[0]
    left_features['proc_ints'] = data[1]
    left_features['dx_masks'] = data[2]
    left_features['proc_masks'] = data[3]
    
    for k, v in left_features.items():
        left_features[k] = v.to(device)
        
    if dtype == 'gct_single':
        left_priors = {}
        left_priors['indices'] = prior_data[0].to(device)
        left_priors['values'] = prior_data[1].to(device)
        
        return left_features, left_priors
    
    if dtype =='single':return left_features
    
    right_features = {}
    right_features['dx_ints'] = data[4]
    right_features['proc_ints'] = data[5]
    right_features['dx_masks'] = data[6]
    right_features['proc_masks'] = data[7]
    
    all_labels = data[8].to(device)
    for k, v in right_features.items():
        right_features[k] = v.to(device)
    
    if dtype == 'normal':
        left_priors = {}
        left_priors['indices'] = prior_data[0].to(device)
        left_priors['values'] = prior_data[1].to(device)

        right_priors = {}        
        right_priors['indices'] = prior_data[2].to(device)
        right_priors['values'] = prior_data[3].to(device)
        return left_features,right_features, all_labels, left_priors,right_priors
    elif dtype == 'mime':
        return left_features,right_features, all_labels

def compute_metrics(true_labels,predict_labels):
    return {'accu':accuracy_score(true_labels,predict_labels),'precision':precision_score(true_labels,predict_labels),'recall':recall_score(true_labels,predict_labels),'f1':f1_score(true_labels,predict_labels)}
"""def compute_metrics(preds, labels):
    metrics = {}
    preds = np.argmax(preds, axis=1)
    # average precision
    ap = average_precision_score(labels, preds)
    # metrics['AP'] = ap
    
    # auprc
    precisions, recalls, thresholds = precision_recall_curve(labels, preds)
    auc_pr = auc(recalls, precisions)
    metrics['AUCPR'] = auc_pr

    
    
    # auroc
    fpr, tpr, thresholds = roc_curve(labels, preds)
    auc_roc = auc(fpr, tpr)
    metrics['AUROC'] = auc_roc

    # f1 score, precision, recall
    precision, recall, fscore, support = precision_recall_fscore_support(labels, preds, average='weighted')
    # metrics['precision'] = precision
    # metrics['recall'] = recall
    # metrics['fscore'] = fscore
    
    
    return metrics"""
"""
for name, param in model.named_parameters():
    print(name, param.is_cuda)
"""
class ArgParser(argparse.ArgumentParser):
    def __init__(self):
        super(ArgParser, self).__init__()
        self.add_argument('--data_dir', type=str, required=True)
        self.add_argument('--output_dir', type=str, required=True)

        self.add_argument('--max_num_codes', type=int, default=100)
        self.add_argument('--feature_keys', action='append', default=['dx_ints','proc_ints'])
        self.add_argument('--vocab_sizes', type=json.loads, default={'dx_ints':1199, 'proc_ints':1800})#for sigir: default={'dx_ints':1299, 'proc_ints':1950} #for mime_sensors:{'dx_ints':1204, 'proc_ints':328}# for gct_sensors{'dx_ints':1199, 'proc_ints':1800}
        self.add_argument('--prior_scalar', type=float, default=0.5)
        
        self.add_argument('--num_stacks', type=int, default=3)
        self.add_argument('--hidden_size', type=int, default=128)
        self.add_argument('--intermediate_size', type=int, default=256)
        self.add_argument('--num_heads', type=int, default=1)
        self.add_argument('--hidden_dropout_prob', type=float, default=0.25)

        
        self.add_argument('--learning_rate', type=float, default=0.001)
        self.add_argument('--eps', type=float, default=1e-8)
        self.add_argument('--batch_size', type=int, default=256)
        self.add_argument('--max_grad_norm', type=float, default=1.0)
        
        self.add_argument('--use_guide', default=False, action='store_true')
        self.add_argument('--use_prior', default=False, action='store_true')
        self.add_argument('--do_query', default=False, action='store_true')
        self.add_argument('--do_cluster', default=False, action='store_true')

        self.add_argument('--output_hidden_states', default=False, action='store_true')
        self.add_argument('--output_attentions', default=False, action='store_true')
        
        self.add_argument('--fold', type=int, default=1)
        self.add_argument('--eval_batch_size', type=int, default=256)
        
        self.add_argument('--warmup', type=float, default=0.05)
        self.add_argument('--logging_steps', type=int, default=100)
        self.add_argument('--max_steps', type=int, default=0)
        self.add_argument('--num_train_epochs', type=int, default=800)
        
#         self.add_argument('--label_key', type=str, default='expired')
#         self.add_argument('--num_labels', type=int, default=2)
        
        self.add_argument('--reg_coef', type=float, default=0)
        self.add_argument('--seed', type=int, default=42)
        
        
        self.add_argument('--do_train', default=False, action='store_true')
        self.add_argument('--do_eval', default=False, action='store_true')
        self.add_argument('--do_test', default=False, action='store_true')
        
        
        

        
        

    def parse_args(self):
        args = super().parse_args()
        return args