package cn.medicine.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.medicine.dao.IQuestionDao;
import cn.medicine.pojo.Question;
import cn.medicine.service.IQuestionService;
import cn.medicine.utils.MyException;

@Service
public class QuestionService implements IQuestionService{
    @Autowired
    @Qualifier("questionDao")
    private IQuestionDao questionDao;

    @Override
    public void add(Question question) throws MyException {
        questionDao.add(question);      
    }

    @Override
    public int delete(long id) throws MyException {
        int result=questionDao.delete(id);
        return result;
    }

    @Override
    public List<Question> getQuestionsByHospitalDepartmentId(long id)
            throws MyException {
        List<Question> questionList=questionDao.getQuestionsByHospitalDepartmentId(id);
        return questionList;
    }

    @Override
    public List<Question> getByUserid(long id) throws MyException {
        List<Question> questionList=questionDao.getByUserid(id);
        return questionList;
    }

    @Override
    public List<Question> getAllSingleChoiceQuestions() throws MyException {
        List<Question> questionList=questionDao.getAllSingleChoiceQuestions();
        return questionList;
    }

    @Override
    public Question get(long id) throws MyException {
        Question q=questionDao.get(id);
        return q;
    }

    @Override
    public Map<Long, Question> getByQuestionidlist(List<Long> questionidList)
            throws MyException {
        List<Question> questionlist=questionDao.getByQuestionidlist(questionidList);
        Map<Long,Question> questionmap=new HashMap<Long,Question>();
        Iterator<Question> it=questionlist.iterator();
        Question q;
        while(it.hasNext()){
            q=it.next();
            questionmap.put(q.getQid(), q);
        }
        return questionmap;
    }

}
