package cn.medicine.dao;

import java.util.List;

import cn.medicine.pojo.Question;
import cn.medicine.utils.MyException;

public interface IQuestionDao {
    /**
     * 
     * @Function:     add 
     * @Description:  增加问题
     *                 <功能详细描述>
     *
     * @param question
     */
    public void add(Question question) throws MyException;
    /**
     * 
     * @Function:     delete 
     * @Description:   根据id删除问题  
     *                 <功能详细描述>
     *
     * @param id
     * @return
     */
    public int delete(long id) throws MyException;
    /**
     * 
     * @Function:     getQuestionsByHospitalDepartmentId 
     * @Description:   根据医院科室id来查询问题  
     *                 <功能详细描述>
     *
     * @param id
     * @return
     */
    public List<Question> getQuestionsByHospitalDepartmentId(long id) throws MyException;
    /**
     * 
     * @Function:     getAllQuestions 
     * @Description:   查询所有的问题  
     *                 <功能详细描述>
     *
     * @return
     */
    public List<Question> getAllSingleChoiceQuestions() throws MyException;
    /**
     * 通过userid来查找该用户提出的问题
     * @param id
     * @return
     */
    public List<Question> getByUserid(long id) throws MyException;
    /**
     * 
     * @Function:     getByQuestionlist 
     * @Description:   根据问题id列表来获取问题集合 
     *                 <功能详细描述>
     *
     * @param questionidList
     * @return
     */
    public List<Question> getByQuestionidlist(List<Long> questionidList) throws MyException;

    /**
     * 根据id查找问题
     * @param id
     * @return
     */
    public Question get(long id) throws MyException;
}
