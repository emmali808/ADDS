package com.java.adds.dao;

import com.java.adds.entity.PatientEntity;
import com.java.adds.entity.QuestionEntity;
import com.java.adds.mapper.PatientMapper;
import com.java.adds.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PatientDao {
    @Autowired
    PatientMapper patientMapper;

    @Autowired
    QuestionMapper questionMapper;

    /**ljy
     * 管理员获取所有病人信息
     */
    public ArrayList<PatientEntity> getAllPatients()
    {
        return patientMapper.getAllPatients();
    }

    /**ljy
     *病人查找自己提交的所有问题
     * @return
     */
    public ArrayList<QuestionEntity> searchMyQuestion(Long uid)
    {
        return questionMapper.searchMyQuestion(uid);
    }

    /**ljy
     *新增question
     * @return
     */
    public boolean addQuestion(QuestionEntity questionEntity)
    {
        questionMapper.addQuestion(questionEntity);
        return true;
    }
}
