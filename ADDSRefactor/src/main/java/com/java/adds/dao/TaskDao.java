package com.java.adds.dao;

import com.java.adds.entity.DeepModelTaskEntity;
import com.java.adds.entity.DeepModelTaskResultEntity;
import com.java.adds.mapper.DeepModelTaskResultMapper;
import com.java.adds.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Deep Learning Task Dao
 * @author QXL
 */
@Repository
public class TaskDao {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private DeepModelTaskResultMapper resultMapper;

    /**
     * Get all Model Evaluation Tasks
     * @param userId user's id
     * @return A DeepModelTaskEntity ArrayList
     */
    public ArrayList<DeepModelTaskEntity> getModelEvaluationTasks(Long userId) {
        return taskMapper.getModelEvaluationTasks(userId);
    }

    /**
     * Get all Auto Selection Tasks
     * @param userId user's id
     * @return A DeepModelTaskEntity ArrayList
     */
    public ArrayList<DeepModelTaskEntity> getAutoSelectionTasks(Long userId) {
        return taskMapper.getAutoSelectionTasks(userId);
    }

    /**
     * Get all Knowledge-Embedding Exploration Tasks
     * @param userId user's id
     * @return A DeepModelTaskEntity ArrayList
     */
    public ArrayList<DeepModelTaskEntity> getKnowledgeEmbeddingExplorationTasks(Long userId) {
        return taskMapper.getKnowledgeEmbeddingExplorationTasks(userId);
    }

    /**
     * Add A Model Evaluation Task
     * @param task Model Evaluation Task Entity
     */
    public Long addModelEvaluationTask(Long userId, DeepModelTaskEntity task) {
        task.setUserId(userId);
        taskMapper.addModelEvaluationTask(task);
        return task.getId();
    }

    /**
     * Add A Auto Selection Task
     * @param task Auto Selection Task Entity
     */
    public Long addAutoSelectionTask(Long userId, DeepModelTaskEntity task) {
        task.setUserId(userId);
        taskMapper.addAutoSelectionTask(task);
        return task.getId();
    }

    /**
     * Add A Knowledge-Embedding Exploration Task
     * @param task Knowledge-Embedding Exploration Task Entity
     */
    public Long addKnowledgeEmbeddingExplorationTask(Long userId, DeepModelTaskEntity task) {
        task.setUserId(userId);
        taskMapper.addKnowledgeEmbeddingExplorationTask(task);
        return task.getId();
    }

    /**
     * QXL
     * Get Model Evaluation Task Result
     * @param taskId task id
     * @return A DeepModelTaskResultEntity
     */
    public DeepModelTaskResultEntity getModelEvaluationTaskResult(Long taskId) {
        return resultMapper.getModelEvaluationTaskResult(taskId);
    }

    /**
     * QXL
     * Get Auto Selection Task Results
     * @param taskId task id
     * @return A DeepModelTaskResultEntity ArrayList
     */
    public ArrayList<DeepModelTaskResultEntity> getAutoSelectionTaskResults(Long taskId) {
        return resultMapper.getAutoSelectionTaskResults(taskId);
    }

    /**
     * QXL
     * Get Knowledge Exploration Task Results
     * @param taskId task id
     * @return A DeepModelTaskResultEntity ArrayList
     */
    public ArrayList<DeepModelTaskResultEntity> getKnowledgeExplorationTaskResults(Long taskId) {
        return resultMapper.getKnowledgeExplorationTaskResults(taskId);
    }

    public List<List<Object>> getDataFormatExcel(Long taskId, int taskType) {
        ArrayList<DeepModelTaskResultEntity> results;
        if (taskType == 2) {
            // 2. Auto Selection Task
            results = resultMapper.getAutoSelectionTaskResults(taskId);
        } else {
            // 3. Knowledge Exploration Task
            results = resultMapper.getKnowledgeExplorationTaskResults(taskId);
        }
        List<List<Object>> excelData = new ArrayList<>();

        List<Object> header = new ArrayList<>();
        header.add("model");
        header.add("n@1");
        header.add("n@3");
        header.add("n@5");
        header.add("n@10");
        header.add("map");
        header.add("r@3");
        header.add("r@5");
        header.add("r@10");
        header.add("p@1");
        header.add("p@3");
        header.add("p@5");
        header.add("p@10");
        excelData.add(header);

        for (DeepModelTaskResultEntity result : results) {
            List<Object> row = new ArrayList<>();
            row.add(result.getModelName());
            row.add(result.getNdcg1());
            row.add(result.getNdcg3());
            row.add(result.getNdcg5());
            row.add(result.getNdcg10());
            row.add(result.getMap());
            row.add(result.getRecall3());
            row.add(result.getRecall5());
            row.add(result.getRecall10());
            row.add(result.getPrecision1());
            row.add(result.getPrecision3());
            row.add(result.getPrecision5());
            row.add(result.getPrecision10());
            excelData.add(row);
        }

        return excelData;
    }
}
