package com.java.adds.service;


import com.java.adds.dao.PatientDao;
import com.java.adds.entity.PatientEntity;
import com.java.adds.entity.QuestionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PatientService {
    @Autowired
    PatientDao patientDao;

    /**ljy
     *新增question
     * @return
     */
    public boolean addQuestion(QuestionEntity questionEntity)
    {
        return patientDao.addQuestion(questionEntity);
    }

    /**ljy
     * 管理员获取所有病人信息
     */
    public ArrayList<PatientEntity> getAllPatients()
    {
        return patientDao.getAllPatients();
    }

    /**ljy
     *病人查找自己提交的所有问题
     * @return
     */
    public ArrayList<QuestionEntity> searchMyQuestion(Long uid)
    {
        return patientDao.searchMyQuestion(uid);
    }
}
