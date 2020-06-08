package cn.medicine.dao;

import java.util.List;

import cn.medicine.pojo.QuestionAndResult;
import cn.medicine.pojo.QuestionResult;
import cn.medicine.utils.MyException;
/**
 * 
 * @author ztxu
 *
 */
public interface IQuestionResultDao {
    
    /**
     * 增加问题答案
     * @param qr
     * @return
     */
    public int add(QuestionResult qr) throws MyException;
    /**
     * 根据id删除问题答案
     * @param id
     * @return
     */
    public int delete(long id) throws MyException;
    /**
     * 根据用户id搜索某个用户的问题答案
     * @param userid
     * @return
     */
    public List<QuestionResult> getByUserid(long userid) throws MyException;
    
    /**
     * 
     * @Function:     getQuestionAndResultByUserid 
     * @Description:   根据用户id搜索某个用户的问题答案及对应的问题  
     *                 <功能详细描述>
     *
     * @param userid
     * @return
     */
    public List<QuestionAndResult> getQuestionAndResultByUserid(long userid) throws MyException;
    /**
     * 根据问题id搜索某个问题的所有答案
     * @param questionid
     * @return
     */
    public List<QuestionResult> getByQuestionid(long questionid) throws MyException;

}
