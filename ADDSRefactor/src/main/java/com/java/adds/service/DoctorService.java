package com.java.adds.service;

import com.java.adds.controller.vo.FilterQuestionVO;
import com.java.adds.controller.vo.QuestionAnswerVO;
import com.java.adds.dao.DoctorDao;
import com.java.adds.entity.DataSetsEntity;
import com.java.adds.entity.DeepModelTaskEntity;
import com.java.adds.entity.DoctorEntity;
import com.java.adds.entity.QuestionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DoctorService {
    @Autowired
    DoctorDao doctorDao;

    /**ljy
     * 管理员获取所有医生信息
     */
    public ArrayList<DoctorEntity> getAllDoctors()
    {
        return doctorDao.getAllDoctors();
    }

    /**ljy
     * 医生获取问题（回答与否，问题类型）
     * @return
     */
    public ArrayList<QuestionEntity> getFilterQuestion(FilterQuestionVO filterQuestionVO, Long doctorId)
    {
        return doctorDao.getFilterQuestion(filterQuestionVO,doctorId);
    }

    /**ljy
     *获取某一个科室下的未回答问题
     * @return
     */
    public ArrayList<QuestionEntity> getQuestionsInHosDepartment(Long uid, Long hdId)
    {
        return doctorDao.getQuestionsInHosDepartment(uid,hdId);
    }

    /**ljy
     * 医生回答某个问题
     * @return
     */
    public boolean insertAnswer(Long uid, Long qid, QuestionAnswerVO questionAnswerVO)
    {
        return doctorDao.insertAnswer(uid,qid, questionAnswerVO);
    }


    /**ljy
     * 医生新建一个数据集
     * @return
     */
    public Integer newDataSet(Integer doctorId, DataSetsEntity dataSetsEntity)
    {
        return doctorDao.newDataSet(doctorId, dataSetsEntity);
    }

    /**ljy
     * 医生上传数据集
     * @return
     */
    public void uploadDataSet(Integer dId,Integer doctorId, String fileName,String filePath,String fileType)
    {
        doctorDao.uploadDataSet(dId,doctorId,fileName,filePath,fileType);
    }


    /**ljy
     * 医生上传知识图谱
     * @return
     */
//    public Long uploadKG(Long doctorId,String fileName,String filePath)
//    {
//        return doctorDao.uploadKG(doctorId,fileName,filePath);
//    }

    /**ljy
     * 医生获取全部数据集
     * @return
     */
    public ArrayList<DataSetsEntity> getDataSets(Long doctorId) {
        return doctorDao.getDataSets(doctorId);
    }

    /**ljy
     * 医生获取可用数据集
     * @return
     */
    public ArrayList<DataSetsEntity> getAvailableDataSets(Long doctorId) {
        return doctorDao.getAvailableDataSets(doctorId);
    }

    /**ljy
     * 医生获获取知识图谱
     * @return
     */
//    public ArrayList<DataSetsEntity> getKGS(Long doctorId)
//    {
//        return doctorDao.getKGS(doctorId);
//    }


    /**ljy
     * 医生运行一个深度学习模型
     * @return
     */
    public void doDeepModelTask(Long doctorId,DeepModelTaskEntity deepModelTaskEntity)
    {
        doctorDao.doDeepModelTask(doctorId,deepModelTaskEntity);
    }

    /**
     * 医生获取所有任务
     * @author ljy
     * @return
     */
    public ArrayList<DeepModelTaskEntity> getDMTasks(Integer doctorId)
    {
        return doctorDao.getDMTasks(doctorId);
    }
}
