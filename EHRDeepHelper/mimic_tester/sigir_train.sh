# for i in `seq 0 2`
# do
# {
#     CUDA_VISIBLE_DEVICES=1 python train.py  --batch_size 256 --num_train_epochs 100 --learning_rate 0.00005 --dtype onto --gcn_conv_nums 1 --train_data no_p_a_dataset/train_remove_addm_undirect_onto_data.pt  --test_data no_p_a_dataset/test_remove_addm_undirect_onto_data.pt  --valid_data no_p_a_dataset/valid_remove_addm_undirect_onto_data.pt   --output_dir drmm_attn_2_$i --use_glb $i  --do_train --do_eval --do_test --match drmm --use_cross_atten > drmm_output/drmm_attn_$i.txt 
# }    &
# done

# for i in `seq 0 2`
# do
# {
#     python train.py  --batch_size 256 --num_train_epochs 100 --learning_rate 0.00005 --dtype onto --gcn_conv_nums 1 --train_data no_p_a_dataset/train_remove_addm_undirect_onto_data.pt  --test_data no_p_a_dataset/test_remove_addm_undirect_onto_data.pt  --valid_data no_p_a_dataset/valid_remove_addm_undirect_onto_data.pt   --output_dir drmm_$i --use_glb $i  --do_train --do_eval --do_test --match drmm > drmm_output/drmm_$i.txt
# }    &
# done

#gat gin gated_conv gcn

# for conv in gat  gated_conv
# do
# {
#     python train.py  --batch_size 256 --num_train_epochs 100 --learning_rate 0.00005 --dtype onto --gcn_conv_nums 1 --train_data no_p_a_dataset/train_remove_addm_undirect_onto_data.pt  --test_data no_p_a_dataset/test_remove_addm_undirect_onto_data.pt  --valid_data no_p_a_dataset/valid_remove_addm_undirect_onto_data.pt   --output_dir drmm_$conv --use_glb 2  --do_train --do_eval --do_test --match drmm --use_cross_atten --use_conv $conv> drmm_output/drmm_$conv.txt 
# }    &
# done

# for conv in gat gated_conv
# do
# {
#     CUDA_VISIBLE_DEVICES=1 python train.py  --batch_size 256 --num_train_epochs 100 --learning_rate 0.00005 --dtype onto --gcn_conv_nums 1 --train_data no_p_a_dataset/train_remove_addm_undirect_onto_data.pt  --test_data no_p_a_dataset/test_remove_addm_undirect_onto_data.pt  --valid_data no_p_a_dataset/valid_remove_addm_undirect_onto_data.pt   --output_dir knrm_$conv --use_glb 2  --do_train --do_eval --do_test --match knrm --use_cross_atten --use_conv $conv> knrm_output/knrm_$conv.txt 
# }    &
# done

# for conv_num in `seq 2 3`
# do
# {
#     python train.py  --batch_size 256 --num_train_epochs 100 --learning_rate 0.00005 --dtype onto --gcn_conv_nums $conv_num --train_data no_p_a_dataset/train_remove_addm_undirect_onto_data.pt  --test_data no_p_a_dataset/test_remove_addm_undirect_onto_data.pt  --valid_data no_p_a_dataset/valid_remove_addm_undirect_onto_data.pt   --output_dir test_$conv_num --use_glb 2  --do_train --do_eval --do_test --match drmm --use_cross_atten --use_conv gated_conv> drmm_output/test_$conv_num.txt 
# }    &
# done

# for conv in gat gated_conv
# do
# {
#     CUDA_VISIBLE_DEVICES=1 python train.py  --batch_size 256 --num_train_epochs 50 --learning_rate 0.00005 --dtype onto --gcn_conv_nums 1 --train_data no_p_a_dataset/train_remove_addm_undirect_onto_data.pt  --test_data no_p_a_dataset/test_remove_addm_undirect_onto_data.pt  --valid_data no_p_a_dataset/valid_remove_addm_undirect_onto_data.pt   --output_dir knrm_$conv --use_glb 2  --do_train --do_eval --do_test --match knrm --use_cross_atten --use_conv gcn> knrm_output/knrm_$conv.txt 
# }    &
# done

# for i in 10 30
# do
# {
#     python train.py --batch_size 256 --num_train_epochs 100 --learning_rate 0.00005 --dtype onto --gcn_conv_nums 1 --train_data no_p_a_dataset/train_remove_addm_undirect_onto_data.pt --test_data no_p_a_dataset/test_remove_addm_undirect_onto_data.pt --valid_data no_p_a_dataset/valid_remove_addm_undirect_onto_data.pt --output_dir test_4_bins_$i --use_emb 2 --use_glb 2 --do_train --do_eval --do_test --match drmm --use_conv gated_conv --bins $i > drmm_output/bins_$i.txt
# } &
# done

# for i in  15 25
# do
# {
#     CUDA_VISIBLE_DEVICES=1 python train.py --batch_size 256 --num_train_epochs 100 --learning_rate 0.00005 --dtype onto --gcn_conv_nums 1 --train_data no_p_a_dataset/train_remove_addm_undirect_onto_data.pt --test_data no_p_a_dataset/test_remove_addm_undirect_onto_data.pt --valid_data no_p_a_dataset/valid_remove_addm_undirect_onto_data.pt --output_dir test_4_bins_$i --use_emb 2 --use_glb 2 --do_train --do_eval --do_test --match drmm --use_conv gated_conv --bins $i > drmm_output/bins_$i.txt
# } &
# done

# for i in 50 150
# do
# {
#     python train.py --batch_size 256 --num_train_epochs 100 --learning_rate 0.00005 --dtype onto --gcn_conv_nums 1 --train_data no_p_a_dataset/train_remove_addm_undirect_onto_data.pt --test_data no_p_a_dataset/test_remove_addm_undirect_onto_data.pt --valid_data no_p_a_dataset/valid_remove_addm_undirect_onto_data.pt --output_dir test_4_dim_$i --use_emb 2 --use_glb 2 --do_train --do_eval --do_test --match drmm --use_conv gated_conv --hidden_size $i --graph_hidden_size $i > drmm_output/dim_$i.txt
# } &
# done

# for i in 200 250 300
# do
# {
#     CUDA_VISIBLE_DEVICES=1 python train.py --batch_size 256 --num_train_epochs 100 --learning_rate 0.00005 --dtype onto --gcn_conv_nums 1 --train_data no_p_a_dataset/train_remove_addm_undirect_onto_data.pt --test_data no_p_a_dataset/test_remove_addm_undirect_onto_data.pt --valid_data no_p_a_dataset/valid_remove_addm_undirect_onto_data.pt --output_dir test_4_dim_$i --use_emb 2 --use_glb 2 --do_train --do_eval --do_test --match drmm --use_conv gated_conv --hidden_size $i --graph_hidden_size $i > drmm_output/dim_$i.txt
# } &
# done

# for query
# python train.py --batch_size 256 --num_train_epochs 100 --learning_rate 0.00005 --dtype onto --gcn_conv_nums 1 --train_data no_p_a_dataset/train_remove_addm_undirect_onto_data.pt --test_data no_p_a_dataset/test_remove_addm_undirect_onto_data.pt --valid_data no_p_a_dataset/valid_remove_addm_undirect_onto_data.pt --output_dir test_4  --do_train --do_eval --do_test --match drmm --use_conv gated_conv --do_query --query_data ../self_dataset/query_case.csv

# for training
# output_dir: which stores the training and test result
# use_glb: use global, local or both information
# gcn_conv_nums: the number of conv layers
python train.py  --batch_size 256 --num_train_epochs 100 --learning_rate 0.00005 --dtype onto --gcn_conv_nums 1 --train_data no_p_a_dataset/train_remove_addm_undirect_onto_data.pt  --test_data no_p_a_dataset/test_remove_addm_undirect_onto_data.pt  --valid_data no_p_a_dataset/valid_remove_addm_undirect_onto_data.pt   --output_dir drmm_2 --use_glb 2  --do_train --do_eval --do_test --match drmm 


#for cluster
"""our model:
python /home/lf/桌面/MIMIC全/mimic_tester/clustering_ev.py --query_data /home/lf/桌面/MIMIC全/self_dataset/cluster_admissions.csv --query_save_data /home/lf/桌面/MIMIC全/mimic_tester/no_p_a_dataset/test_query_remove_addm_undirect_onto_data.pt
gram:
python /home/lf/桌面/MIMIC全/mimic_tester/clustering_ev.py --query_data /home/lf/桌面/MIMIC全/self_dataset/cluster_admissions.csv --query_save_data /home/lf/桌面/MIMIC全/mimic_tester/no_p_a_dataset/test_query_remove_addm_undirect_onto_data.pt --output_dir gram/
gct:
python train.py --data_dir ../../../self_dataset/  --output_dir output_res/   --use_prior --use_guide  --do_cluster
patient_similarity:
python train.py
simgnn:
python main.py --do_cluster  --cluster-graphs ../../../no_p_a_dataset/test_query_remove_addm_undirect_onto_data.pt --training-graphs ../../../no_p_a_dataset/train_remove_addm_undirect_onto_data.pt  --testing-graphs ../../../no_p_a_dataset/test_remove_addm_undirect_onto_data.pt --valid-graphs ../../../no_p_a_dataset/valid_remove_addm_undirect_onto_data.pt
graph2vec:
python graph2vec.py --input-path ../mimic_test_query_dataset/"""


# python train.py  --batch_size 256 --num_train_epochs 50 --learning_rate 0.00005 --dtype onto --gcn_conv_nums 1 --train_data no_p_a_dataset/train_remove_addm_undirect_onto_data.pt  --test_data no_p_a_dataset/test_remove_addm_undirect_onto_data.pt  --valid_data no_p_a_dataset/valid_remove_addm_undirect_onto_data.pt   --output_dir knrm_2 --use_glb 2  --do_train --do_eval --do_test --match knrm --use_cross_atten --use_conv gated_conv

# python train.py --batch_size 256 --num_train_epochs 100 --learning_rate 0.00005 --dtype onto --gcn_conv_nums 1 --train_data p_a_dataset/train_remove_addm_undirect_onto_data.pt --test_data p_a_dataset/test_remove_addm_undirect_onto_data.pt --valid_data p_a_dataset/valid_remove_addm_undirect_onto_data.pt --output_dir drmm_2  --do_train --do_eval --do_test --match drmm --use_conv gated_conv