package com.java.adds.dao;

import com.java.adds.entity.ChoiceAnswerEntity;
import com.java.adds.entity.DetailAnswerEntity;
import com.java.adds.entity.QuestionEntity;
import com.java.adds.mapper.QuestionDetailAnswerMapper;
import com.java.adds.mapper.QuestionMapper;
import com.java.adds.mapper.QuestionResultMapper;
import com.java.adds.deepAlgorithm.QA_score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class QADao {
    @Autowired
    QuestionDetailAnswerMapper questionDetailAnswerMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuestionResultMapper questionResultMapper;

    @Autowired
    QA_score qa_score;

    /**ljy
     *根据id删除question
     * @return
     */
    public boolean deleteQuestion(Long qid)
    {
        questionMapper.deleteQuestion(qid);  //删除问题
        //questionResultMapper.deleteAnswersByQid(qid); //删除问题所对应的回答

        return true;
    }

    /**ljy
     *根据问题id查找答案
     * @return
     */
    public ChoiceAnswerEntity searchChoiceAnswerById(Long qid)
    {
        ChoiceAnswerEntity choiceAnswerEntity=new ChoiceAnswerEntity();
        choiceAnswerEntity.setQid(qid);
        choiceAnswerEntity.setContent(questionMapper.getQuestionById(qid).getContent());
        ArrayList<Integer> answerList=questionResultMapper.searchChoiceAnswerById(qid);
        int yesA=0,noA=0;
        for(int i=0;i<answerList.size();i++)  //1:yes 2:no
        {
            if(answerList.get(i)==1)
                yesA++;
            else
                noA++;
        }
        choiceAnswerEntity.setYesCount(yesA);
        choiceAnswerEntity.setNoCount(noA);
        return choiceAnswerEntity;
    }

    /**ljy
     *根据问题id查找所有的详细回答
     * @return
     */
    public ArrayList<DetailAnswerEntity> searchDetailAnswerById(Long qid)
    {
        return questionDetailAnswerMapper.searchDetailAnswerById(qid);
    }

    /**ljy
     *QA检索
     * @return
     */
    public ArrayList<QuestionEntity> searchSimpleQuestion(String question)
    {
        //ArrayList<QuestionEntity> allQuestion=questionMapper.getAllQuestion();
        ArrayList<QuestionEntity> allQuestion=questionMapper.getAllQuestionTest();
        String[] doc=new String[allQuestion.size()];
        for(int i=0;i<allQuestion.size();i++)
            doc[i]=allQuestion.get(i).getContent();
        //调用学姐的代码
        String[] que={question};
        String temResult=qa_score.do_QA_research(que,doc);
        String searchResult=temResult.substring(2,temResult.length()-3);
        String[] index=searchResult.split(", ");
        ArrayList<QuestionEntity> result=new ArrayList<>();
        for(int i=0;i<index.length;i++)
            result.add(allQuestion.get(Integer.parseInt(index[i])));


        return result;
    }
}
