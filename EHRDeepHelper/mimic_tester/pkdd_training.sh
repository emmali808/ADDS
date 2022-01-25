# for i in 50  150
# do
# {
#     python train_cluster.py --batch_size 256 --num_train_epochs 50 --learning_rate 0.00005 --train_data all_dataset/train_pa_include_remove_addm_undirect_onto_data.pt --test_data all_dataset/test_pa_include_remove_addm_undirect_onto_data.pt --valid_data all_dataset/valid_pa_include_remove_addm_undirect_onto_data.pt --output_dir test_emb_$i --use_glb 2 --do_train --do_eval --do_test --train_input_data all_dataset/train_saved_pa_include_remove_addm_undirect_onto_data.pt --use_emb 2  --hidden_size $i > emb_output_$i.txt
# } &
# done

# for i in 200 250 300
# do
# {
#     CUDA_VISIBLE_DEVICES=1 python train_cluster.py --batch_size 256 --num_train_epochs 50 --learning_rate 0.00005 --train_data all_dataset/train_pa_include_remove_addm_undirect_onto_data.pt --test_data all_dataset/test_pa_include_remove_addm_undirect_onto_data.pt --valid_data all_dataset/valid_pa_include_remove_addm_undirect_onto_data.pt --output_dir test_emb_$i --use_glb 2 --do_train --do_eval --do_test --train_input_data all_dataset/train_saved_pa_include_remove_addm_undirect_onto_data.pt --use_emb 2  --hidden_size $i > emb_output_$i.txt
# } &
# done

# for i in 5 10 15 20
# do
# {
#     python train_cluster.py --batch_size 256 --num_train_epochs 50 --learning_rate 0.00005 --train_data all_dataset/train_pa_include_remove_addm_undirect_onto_data.pt --test_data all_dataset/test_pa_include_remove_addm_undirect_onto_data.pt --valid_data all_dataset/valid_pa_include_remove_addm_undirect_onto_data.pt --output_dir test_neuron_$i --use_glb 2 --do_train --do_eval --do_test --train_input_data all_dataset/train_saved_pa_include_remove_addm_undirect_onto_data.pt --use_emb 2  --pair_neurons $i > neuron_output_$i.txt
# } &
# done

# for i in 25 35 40
# do
# {
#     CUDA_VISIBLE_DEVICES=1 python train_cluster.py --batch_size 256 --num_train_epochs 50 --learning_rate 0.00005 --train_data all_dataset/train_pa_include_remove_addm_undirect_onto_data.pt --test_data all_dataset/test_pa_include_remove_addm_undirect_onto_data.pt --valid_data all_dataset/valid_pa_include_remove_addm_undirect_onto_data.pt --output_dir test_neuron_$i --use_glb 2 --do_train --do_eval --do_test --train_input_data all_dataset/train_saved_pa_include_remove_addm_undirect_onto_data.pt --use_emb 2  --pair_neurons $i > neuron_output_$i.txt
# } &
# done

"""for i in 0 1 3
do
{
    python train_cluster.py --batch_size 256 --num_train_epochs 50 --learning_rate 0.00005 --train_data all_dataset/train_pa_include_remove_addm_undirect_onto_data.pt --test_data all_dataset/test_pa_include_remove_addm_undirect_onto_data.pt --valid_data all_dataset/valid_pa_include_remove_addm_undirect_onto_data.pt --output_dir test_emb_$i --use_glb 2 --do_train --do_eval --do_test --train_input_data all_dataset/train_saved_pa_include_remove_addm_undirect_onto_data.pt --use_emb $i > emb_output_$i.txt
} &
done

for i in 4 5 6
do
{
    CUDA_VISIBLE_DEVICES=1 python train_cluster.py --batch_size 256 --num_train_epochs 50 --learning_rate 0.00005 --train_data all_dataset/train_pa_include_remove_addm_undirect_onto_data.pt --test_data all_dataset/test_pa_include_remove_addm_undirect_onto_data.pt --valid_data all_dataset/valid_pa_include_remove_addm_undirect_onto_data.pt --output_dir test_emb_$i --use_glb 2 --do_train --do_eval --do_test --train_input_data all_dataset/train_saved_pa_include_remove_addm_undirect_onto_data.pt --use_emb $i > emb_output_$i.txt
} &
done

python train_cluster.py --batch_size 256 --num_train_epochs 50 --learning_rate 0.00005 --train_data all_dataset/train_pa_include_remove_addm_undirect_onto_data.pt --test_data all_dataset/test_pa_include_remove_addm_undirect_onto_data.pt --valid_data all_dataset/valid_pa_include_remove_addm_undirect_onto_data.pt  --use_glb 2 --do_train --do_eval --do_test --train_input_data all_dataset/train_saved_pa_include_remove_addm_undirect_onto_data.pt --query --use_emb 2 --output_dir 

#for demonstration

python train_cluster.py --batch_size 256 --train_data all_dataset/train_pa_include_remove_addm_undirect_onto_data.pt --test_data all_dataset/test_pa_include_remove_addm_undirect_onto_data.pt --valid_data all_dataset/valid_pa_include_remove_addm_undirect_onto_data.pt  --use_glb 2 --train_input_data all_dataset/train_saved_pa_include_remove_addm_undirect_onto_data.pt --use_emb 2 --output_dir pkdd_output/test_conv_2 --do_cluster_query --cluster_nums 2 --cluster_res_path cluster_res.txt --query_save_path ../large_cluster_dataset/query_admissions.csv

需要配置的参数：
cluster_nums 疾病种类
query_save_path:query path 查询的病历文件位置
cluster_res_path: output path->each hadm_id for label, namely clustering report 每行对应一个hadm_id 一个聚类的类别标签"""

# for training
# params -> use_emb, use_glb, output_dir
# use_emb for multi-modal embedding
# use_glb for global, local or both information
# output_dir for model saved path
python train_cluster.py --batch_size 256 --num_train_epochs 50 --learning_rate 0.00005 --train_data all_dataset/train_pa_include_remove_addm_undirect_onto_data.pt --test_data all_dataset/test_pa_include_remove_addm_undirect_onto_data.pt --valid_data all_dataset/valid_pa_include_remove_addm_undirect_onto_data.pt --output_dir test --use_glb 2 --do_train --do_eval --do_test --train_input_data all_dataset/train_saved_pa_include_remove_addm_undirect_onto_data.pt --use_emb 2

