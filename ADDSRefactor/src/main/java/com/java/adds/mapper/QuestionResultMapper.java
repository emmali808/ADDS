package com.java.adds.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface QuestionResultMapper {

    /**ljy
     *根据id删除question对应的回答
     * @return
     */
    public boolean deleteAnswersByQid(@Param("qid") Long qid);

    /**ljy
     *根据问题id查找答案
     * @return
     */
    public ArrayList<Integer> searchChoiceAnswerById(@Param("qid") Long qid);

    /**ljy
     * 医生回答某个问题
     * @return
     */
    public boolean insertChoiceAnswer(@Param("uid") Long uid, @Param("qid")Long qid, @Param("answer")Integer answer,@Param("remark")String remark);

}
