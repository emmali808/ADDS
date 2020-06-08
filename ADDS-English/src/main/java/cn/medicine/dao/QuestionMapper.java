package cn.medicine.dao;

import java.util.List;

import cn.medicine.pojo.Question;

public interface QuestionMapper {
    /**
     * 
     * @Function:?????add?
     * @Description:??增加问题
     *                 <功能详细描述>
     *
     * @param question
     */
    public void add(Question question);
    /**
     * 
     * @Function:?????delete?
     * @Description:???根据id删除问题??
     *                 <功能详细描述>
     *
     * @param id
     * @return
     */
    public int delete(long id);
    /**
     *
     *                 <功能详细描述>
     *
     * @param id
     * @return
     */
    public List<Question> getQuestionsByHospitalDepartmentId(long id);
    /**
     *
     *                 <功能详细描述>
     *
     * @return
     */
    public List<Question> getAllSingleChoiceQuestions();

    /**
     * 通过userid来查找该用户提出的问题
     * @param id
     * @return
     */
    public List<Question> getByUserid(long id);
    /**
     *
     *                 <功能详细描述>
     *
     * @param questionidList
     * @return
     */
    public List<Question> getByQuestionidlist(List<Long> questionidList);

    /**
     * 根据id查找问题
     * @param id
     * @return
     */
    public Question get(long id);

}
