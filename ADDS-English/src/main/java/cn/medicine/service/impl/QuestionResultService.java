package cn.medicine.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.medicine.dao.IQuestionResultDao;
import cn.medicine.pojo.QuestionAndResult;
import cn.medicine.pojo.QuestionResult;
import cn.medicine.service.IQuestionResultService;
import cn.medicine.utils.MyException;

@Service
public class QuestionResultService implements IQuestionResultService{
    
    @Autowired
    private IQuestionResultDao questionResultDao;

    @Override
    public int add(QuestionResult qr) throws MyException {
        int result=questionResultDao.add(qr);
        return result;
    }

    @Override
    public int delete(long id) throws MyException {
        int result=questionResultDao.delete(id);
        return result;
    }

    @Override
    public List<QuestionResult> getByUserid(long userid) throws MyException {
        List<QuestionResult> qrlist=questionResultDao.getByUserid(userid);
        return qrlist;
    }

    @Override
    public List<QuestionResult> getByQuestionid(long questionid)
            throws MyException {
        List<QuestionResult> qrlist=questionResultDao.getByQuestionid(questionid);
        return qrlist;
    }

    @Override
    public List<QuestionAndResult> getQuestionAndResultByUserid(long userid)
            throws MyException {
        List<QuestionAndResult> qarList=questionResultDao.getQuestionAndResultByUserid(userid);
        return qarList;
    }

}
