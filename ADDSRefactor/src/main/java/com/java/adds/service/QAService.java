package com.java.adds.service;

import com.java.adds.dao.QADao;
import com.java.adds.entity.ChoiceAnswerEntity;
import com.java.adds.entity.DetailAnswerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class QAService {
    @Autowired
    QADao qaDao;

    /**ljy
     *根据问题id查找答案
     * @return
     */
    public ChoiceAnswerEntity searchChoiceAnswerById(Long qid)
    {
        return qaDao.searchChoiceAnswerById(qid);
    }


    /**ljy
     *根据id删除question
     * @return
     */
    public boolean deleteQuestion(Long qid)
    {
        return qaDao.deleteQuestion(qid);
    }

    /**ljy
     *根据问题id查找所有的详细回答
     * @return
     */
    public ArrayList<DetailAnswerEntity> searchDetailAnswerById(Long qid)
    {
        return qaDao.searchDetailAnswerById(qid);
    }
}
