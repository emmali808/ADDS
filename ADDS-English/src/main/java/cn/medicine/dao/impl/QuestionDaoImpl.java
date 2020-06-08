package cn.medicine.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.medicine.dao.IQuestionDao;
import cn.medicine.dao.QuestionMapper;
import cn.medicine.pojo.Question;
import cn.medicine.utils.MyException;

public class QuestionDaoImpl implements IQuestionDao{
    private final static Logger logger= LogManager.getLogger(QuestionDaoImpl.class);
    
    private boolean isDebugEnable=logger.isDebugEnabled();

    private boolean isErrorEnable=logger.isErrorEnabled();
    
    private QuestionMapper questionMapper;
    
    public QuestionDaoImpl(){
        super();
    }
    
    public QuestionDaoImpl(QuestionMapper questionMapper){
        super();
        this.questionMapper=questionMapper;
    }

    @Override
    public void add(Question question) throws MyException {
        try{
        	questionMapper.add(question);
        }catch(Exception e){
        	if(isErrorEnable){
        		logger.error("添加问题异常,question:"+question.toString());
        		throw new MyException(e.getMessage());
        	}
        }
        
    }

    @Override
    public int delete(long id) throws MyException {
        try{
        	int result=questionMapper.delete(id);
        	return result;
        }catch(Exception e){
        	if(isErrorEnable){
        		logger.error("删除问题异常，id="+id);
        		throw new MyException(e.getMessage());
        	}
        }
       
        return 0;
    }

    @Override
    public List<Question> getQuestionsByHospitalDepartmentId(long id) throws MyException {
    	List<Question> questionList;
        try{
        	questionList=questionMapper.getQuestionsByHospitalDepartmentId(id);
        	return questionList;
        }catch(Exception e){
        	if(isErrorEnable){
        		logger.error("通过医院部门id获取对应问题失败，其中部门id="+id);
        	}
        	throw new MyException(e.getMessage());
        }
    }

    @Override
    public List<Question> getAllSingleChoiceQuestions() throws MyException {
        try{
        	List<Question> questionList=questionMapper.getAllSingleChoiceQuestions();
        	return questionList;
        }catch(Exception e){
        	if(isErrorEnable){
        		logger.error("获取所有的单选问题失败");
        	}
        	throw new MyException(e.getMessage());
        }
    }

    @Override
    public List<Question> getByUserid(long id) throws MyException {
        try{
            List<Question> questionList=questionMapper.getByUserid(id);
            return questionList;
        }catch (Exception e){
            if(isErrorEnable){
                logger.error("获取某用户提出的问题失败，userid="+id);
            }
            throw new MyException("获取某用户提出的问题失败，userid="+id+"错误信息："+e.getMessage());
        }
    }

    @Override
    public Question get(long id) throws MyException {
        try{
            Question q=questionMapper.get(id);
            return q;
        }catch (Exception e){
            if(isErrorEnable){
                logger.error("获取问题失败,id="+id);
            }
            throw new MyException("获取问题失败,id="+id+",错误信息"+e.getMessage());
        }
    }

    @Override
    public List<Question> getByQuestionidlist(List<Long> questionidList)
            throws MyException {
        try{
            List<Question> questions=questionMapper.getByQuestionidlist(questionidList);
            return questions;
        }catch(Exception e){
            if(isErrorEnable){
                logger.error("通过问题id集合获取问题集合失败,questionidList="+questionidList.toString());
            }
            throw new MyException("通过问题id集合获取问题集合失败,questionidList="+questionidList.toString()+",错误信息"+e.getMessage());
        }
    }

}
