package com.java.adds.service;

import com.java.adds.dao.TaskDao;
import com.java.adds.entity.DeepModelTaskEntity;
import com.java.adds.entity.DeepModelTaskResultEntity;
import com.java.adds.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Deep Learning Task Service
 * @author QXL
 */
@Service
public class TaskService {

    @Autowired
    private TaskDao taskDao;

    public String exportResultData(Long taskId, int taskType) {
//        ArrayList<DeepModelTaskResultEntity> results;
//        if (taskType == 1) {
//            // 1. Auto Selection Task
//            results = taskDao.getAutoSelectionTaskResults(taskId);
//        } else {
//            // 2. Knowledge Exploration Task
//            results = taskDao.getKnowledgeExplorationTaskResults(taskId);
//        }
        return "hello";
    }

    public void exportResult(HttpServletResponse response, Long taskId, int taskType) throws IOException {
        // 2. Auto Selection Task
        // 3. Knowledge Exploration Task
        List<List<Object>> excelData = taskDao.getDataFormatExcel(taskId, taskType);
        FileUtil.exportDataToExcel(response.getOutputStream(), excelData, "results", 10);
    }

    /**
     * QXL
     * Get Model Evaluation Task Result
     * @param taskId task id
     * @return A DeepModelTaskResultEntity
     */
    public DeepModelTaskResultEntity getModelEvaluationTaskResult(Long taskId) {
        return taskDao.getModelEvaluationTaskResult(taskId);
    }

    /**
     * QXL
     * Get Auto Selection Task Results
     * @param taskId task id
     * @return A DeepModelTaskResultEntity ArrayList
     */
    public ArrayList<DeepModelTaskResultEntity> getAutoSelectionTaskResults(Long taskId) {
        return taskDao.getAutoSelectionTaskResults(taskId);
    }

    /**
     * QXL
     * Get Knowledge Exploration Task Results
     * @param taskId task id
     * @return A DeepModelTaskResultEntity ArrayList
     */
    public ArrayList<DeepModelTaskResultEntity> getKnowledgeExplorationTaskResults(Long taskId) {
        return taskDao.getKnowledgeExplorationTaskResults(taskId);
    }

    /**
     * Get all Model Evaluation Tasks
     * @param userId user's id
     * @return A DeepModelTaskEntity ArrayList
     */
    public ArrayList<DeepModelTaskEntity> getModelEvaluationTasks(Long userId) {
        return taskDao.getModelEvaluationTasks(userId);
    }

    /**
     * Add A Model Evaluation Task
     * @param task Model Evaluation Task Entity
     */
    public Long addModelEvaluationTask(Long userId, DeepModelTaskEntity task) {
        return taskDao.addModelEvaluationTask(userId, task);
    }

    /**
     * Get all Auto Selection Tasks
     * @param userId user's id
     * @return A DeepModelTaskEntity ArrayList
     */
    public ArrayList<DeepModelTaskEntity> getAutoSelectionTasks(Long userId) {
        return taskDao.getAutoSelectionTasks(userId);
    }

    /**
     * Add A Auto Selection Task
     * @param task Auto Selection Task Entity
     */
    public Long addAutoSelectionTask(Long userId, DeepModelTaskEntity task) {
        return taskDao.addAutoSelectionTask(userId, task);
    }

    /**
     * Get all Knowledge-Embedding Exploration Tasks
     * @param userId user's id
     * @return A DeepModelTaskEntity ArrayList
     */
    public ArrayList<DeepModelTaskEntity> getKnowledgeEmbeddingExplorationTasks(Long userId) {
        return taskDao.getKnowledgeEmbeddingExplorationTasks(userId);
    }

    /**
     * Add A Knowledge-Embedding Exploration Task
     * @param task Knowledge-Embedding Exploration Task Entity
     */
    public Long addKnowledgeEmbeddingExplorationTask(Long userId, DeepModelTaskEntity task) {
        return taskDao.addKnowledgeEmbeddingExplorationTask(userId, task);
    }
}
