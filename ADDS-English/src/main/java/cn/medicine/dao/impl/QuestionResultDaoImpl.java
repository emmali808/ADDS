package cn.medicine.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.medicine.dao.IQuestionResultDao;
import cn.medicine.dao.QuestionResultMapper;
import cn.medicine.pojo.QuestionAndResult;
import cn.medicine.pojo.QuestionResult;
import cn.medicine.utils.MyException;

@Repository
public class QuestionResultDaoImpl implements IQuestionResultDao{
    private final static Logger logger= LogManager.getLogger(QuestionResultDaoImpl.class);
    
    private boolean isDebugEnable=logger.isDebugEnabled();

    private boolean isErrorEnable=logger.isErrorEnabled();
    
    @Autowired
    private QuestionResultMapper questionResultMapper;
    
    public QuestionResultDaoImpl(){
        super();
    }
    
    public QuestionResultDaoImpl(QuestionResultMapper questionResultMapper){
        this.questionResultMapper=questionResultMapper;
    }

    @Override
    public int add(QuestionResult qr) throws MyException {
        try{
            int result=questionResultMapper.add(qr);
            return result;
        }catch(Exception e){
            if(isErrorEnable){
                logger.error("添加问题答案失败，questionresult="+qr.toString());
            }
            throw new MyException(e.getMessage());
        }
    }

    @Override
    public int delete(long id) throws MyException {
        try{
            int result=questionResultMapper.delete(id);
            return result;
        }catch(Exception e){
            if(isErrorEnable){
                logger.error("删除问题答案失败，id="+id);
            }
            throw new MyException(e.getMessage());
        }
    }

    @Override
    public List<QuestionResult> getByUserid(long userid) throws MyException {
        try{
            List<QuestionResult> qrs=questionResultMapper.getByUserid(userid);
            return qrs;
        }catch(Exception e){
            if(isErrorEnable){
                logger.error("通过用户id获取其对应的问题答案失败，其中userid="+userid);
            }
            throw new MyException(e.getMessage());
        }
    }

    @Override
    public List<QuestionResult> getByQuestionid(long questionid)
            throws MyException {
        try{
            List<QuestionResult> qrs=questionResultMapper.getByQuestionid(questionid);
            return qrs;
        }catch(Exception e){
            if(isErrorEnable){
                logger.error("通过问题id获取其对应的所有问题答案失败，其中questionid="+questionid);
            }
            throw new MyException(e.getMessage());
        }
    }

    @Override
    public List<QuestionAndResult> getQuestionAndResultByUserid(long userid)
            throws MyException {
        try{
            List<QuestionAndResult> qars=questionResultMapper.getQuestionAndResultByUserid(userid);
            return qars;
        }catch(Exception e){
            if(isErrorEnable){
                logger.error("根据用户id搜索某个用户的问题答案及对应的问题，其中userid="+userid);
            }
            throw new MyException(e.getMessage());
        }
    }       
}
