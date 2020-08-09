package com.java.adds.controller;

import com.java.adds.entity.DeepModelTaskEntity;
import com.java.adds.entity.DeepModelTaskResultEntity;
import com.java.adds.security.annotation.PassToken;
import com.java.adds.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;

/**
 * Deep Learning Task Controller
 * @author QXL
 */
@RestController
@RequestMapping(value = "/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * QXL
     * File download test api
     * @param response HTTP Response
     */
//    @RequestMapping(value = "/autoSelectionResult/download/{taskId}", method = RequestMethod.GET)
//    public void downloadTaskResult(HttpServletResponse response, @PathVariable("taskId") Long taskId) throws IOException {
//        File file = new File("/Users/liam/Desktop/testkg/kg_1.csv");
//        FileInputStream fis = new FileInputStream(file);
//        response.setContentType("application/force-download");
//        response.addHeader("Content-disposition", "attachment;fileName=" + "kg_1.csv");
//        OutputStream os = response.getOutputStream();
//        byte[] buf = new byte[1024];
//        int len = 0;
//        while ((len = fis.read(buf)) != -1) {
//            os.write(buf, 0, len);
//        }
//        fis.close();
//    }

    @RequestMapping(value = "/autoSelection/downloadResult/{taskId}", method = RequestMethod.GET)
    public void downloadAutoSelectionTaskResult(HttpServletResponse response, @PathVariable("taskId") Long taskId) throws IOException {
//        response.setContentType("application/force-download");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-disposition", "attachment;fileName=" + "Result.xls");
        response.flushBuffer();
        // Task Type 2 is Auto Selection Task
        taskService.exportResult(response, taskId, 2);
    }

    @RequestMapping(value = "/knowledgeExploration/downloadResult/{taskId}", method = RequestMethod.GET)
    public void downloadKnowledgeExplorationTaskResult(HttpServletResponse response, @PathVariable("taskId") Long taskId) throws IOException {
//        response.setContentType("application/force-download");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-disposition", "attachment;fileName=" + "Result.xls");
        response.flushBuffer();
        // Task Type 3 is Knowledge Exploration Task
        taskService.exportResult(response, taskId, 3);
    }

    /**
     * QXL
     * Get Model Evaluation Task Result
     * @param taskId task id
     * @return A DeepModelTaskResultEntity
     */
    @RequestMapping(value = "/modelEvaluationResult/{taskId}", method = RequestMethod.GET)
    public DeepModelTaskResultEntity getModelEvaluationTaskResult(@PathVariable("taskId") Long taskId) {
        return taskService.getModelEvaluationTaskResult(taskId);
    }

    /**
     * QXL
     * Get Auto Selection Task Results
     * @param taskId task id
     * @return A DeepModelTaskResultEntity ArrayList
     */
    @RequestMapping(value = "/autoSelectionResult/{taskId}", method = RequestMethod.GET)
    public ArrayList<DeepModelTaskResultEntity> getAutoSelectionTaskResults(@PathVariable("taskId") Long taskId) {
        return taskService.getAutoSelectionTaskResults(taskId);
    }

    /**
     * QXL
     * Get Knowledge Exploration Task Results
     * @param taskId task id
     * @return A DeepModelTaskResultEntity ArrayList
     */
    @RequestMapping(value = "/knowledgeExplorationResult/{taskId}", method = RequestMethod.GET)
    public ArrayList<DeepModelTaskResultEntity> getKnowledgeExplorationTaskResults(@PathVariable("taskId") Long taskId) {
        return taskService.getKnowledgeExplorationTaskResults(taskId);
    }

    /**
     * Get all Model Evaluation Tasks
     * @param doctorId user(doctor)'s id
     * @return A DeepModelTaskEntity ArrayList
     */
    @RequestMapping(value = "/{doctorId}/modelEvaluation", method = RequestMethod.GET)
    public ArrayList<DeepModelTaskEntity> getModelEvaluationTasks(@PathVariable("doctorId") Long doctorId) {
        return taskService.getModelEvaluationTasks(doctorId);
    }

    /**
     * Add A Model Evaluation Task
     * @param task Model Evaluation Task Entity
     */
    @RequestMapping(value = "/{doctorId}/modelEvaluation", method = RequestMethod.POST)
    public Long addModelEvaluationTask(@PathVariable("doctorId") Long doctorId, @RequestBody DeepModelTaskEntity task) {
        return taskService.addModelEvaluationTask(doctorId, task);
    }

    /**
     * Get all Auto Selection Tasks
     * @param doctorId user(doctor)'s id
     * @return A DeepModelTaskEntity ArrayList
     */
    @RequestMapping(value = "/{doctorId}/autoSelection", method = RequestMethod.GET)
    public ArrayList<DeepModelTaskEntity> getAutoSelectionTasks(@PathVariable("doctorId") Long doctorId) {
        return taskService.getAutoSelectionTasks(doctorId);
    }

    /**
     * Add A Auto Selection Task
     * @param task Auto Selection Task Entity
     */
    @RequestMapping(value = "/{doctorId}/autoSelection", method = RequestMethod.POST)
    public Long addAutoSelectionTask(@PathVariable("doctorId") Long doctorId, @RequestBody DeepModelTaskEntity task) {
        return taskService.addAutoSelectionTask(doctorId, task);
    }

    /**
     * Get all Knowledge-Embedding Exploration Tasks
     * @param doctorId user(doctor)'s id
     * @return A DeepModelTaskEntity ArrayList
     */
    @RequestMapping(value = "/{doctorId}/knowledgeExploration", method = RequestMethod.GET)
    public ArrayList<DeepModelTaskEntity> getKnowledgeEmbeddingExplorationTasks(@PathVariable("doctorId") Long doctorId) {
        return taskService.getKnowledgeEmbeddingExplorationTasks(doctorId);
    }

    /**
     * Add A Knowledge-Embedding Exploration Task
     * @param task Knowledge-Embedding Exploration Task Entity
     */
    @RequestMapping(value = "/{doctorId}/knowledgeExploration", method = RequestMethod.POST)
    public Long addKnowledgeEmbeddingExplorationTask(@PathVariable("doctorId") Long doctorId, @RequestBody DeepModelTaskEntity task) {
        return taskService.addKnowledgeEmbeddingExplorationTask(doctorId, task);
    }
}
