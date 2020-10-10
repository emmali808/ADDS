package com.java.adds.mapper;

import com.java.adds.entity.DeepModelTaskResultEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface DeepModelTaskResultMapper {

    /**
     * QXL
     * Get Model Evaluation Task Result
     * @param taskId task id
     * @return A DeepModelTaskResultEntity
     */
    @Select("SELECT r.*, m.model_name " +
            "FROM " +
            "deep_model_task_result r LEFT JOIN deep_model m " +
            "ON r.model_id=m.id " +
            "WHERE r.task_id=#{taskId}")
    @Results(id = "taskResultMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "taskId", column = "task_id"),
            @Result(property = "modelId", column = "model_id"),
            @Result(property = "modelName", column = "model_name"),
            @Result(property = "ndcg1", column = "ndcg@1"),
            @Result(property = "ndcg3", column = "ndcg@3"),
            @Result(property = "ndcg5", column = "ndcg@5"),
            @Result(property = "ndcg10", column = "ndcg@10"),
            @Result(property = "map", column = "map"),
            @Result(property = "recall3", column = "recall@3"),
            @Result(property = "recall5", column = "recall@5"),
            @Result(property = "recall10", column = "recall@10"),
            @Result(property = "precision1", column = "pre@1"),
            @Result(property = "precision3", column = "pre@3"),
            @Result(property = "precision5", column = "pre@5"),
            @Result(property = "precision10", column = "pre@10")
    })
    DeepModelTaskResultEntity getModelEvaluationTaskResult(@Param("taskId") Long taskId);

    /**
     * QXL
     * Get Auto Selection Task Results
     * @param taskId task id
     * @return A DeepModelTaskResultEntity ArrayList
     */
    @Select("SELECT r.*, m.model_name " +
            "FROM " +
            "deep_model_task_result r LEFT JOIN deep_model m " +
            "ON r.model_id=m.id " +
            "WHERE r.task_id=#{taskId}")
    @ResultMap(value = "taskResultMap")
    ArrayList<DeepModelTaskResultEntity> getAutoSelectionTaskResults(@Param("taskId") Long taskId);

    /**
     * QXL
     * Get Knowledge Exploration Task Results
     * @param taskId task id
     * @return A DeepModelTaskResultEntity ArrayList
     */
    @Select("SELECT r.*, m.model_name " +
            "FROM " +
            "deep_model_task_result r LEFT JOIN deep_model m " +
            "ON r.model_id=m.id " +
            "WHERE r.task_id=#{taskId}")
    @ResultMap(value = "taskResultMap")
    ArrayList<DeepModelTaskResultEntity> getKnowledgeExplorationTaskResults(@Param("taskId") Long taskId);

    /**ljy
     * 插入模型运行结果
     * @return
     */
    Long insertDeepModelTaskResult(@Param("deepModelTaskResultEntity") DeepModelTaskResultEntity deepModelTaskResultEntity);
}
