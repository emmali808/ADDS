package cn.medicine.pojo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
/**
 * 
 * @ClassName:QuestionAndResult
 * @Description: 在线问答系统，用户的回答结果
 * @Function List:TODO 主要函数及其功能
 *
 * @author   ztxu
 * @version  
 * @Date	 2016-8-21下午5:00:20
 *
 * @History:// 历史修改记录 
 * <author>  // 修改人
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述修改内容
 */
public class QuestionAndResult implements Serializable{

    private static final long serialVersionUID = -3870290454240738607L;
    private long qrid;//问题答案id
    private long qid;//问题id
    private int resultType;//单选题的回答，1：Yes  2：No
    private String detailedAnswer;//详细回答答案
    private String remark;//备注
    private String content;//问题内容
    
    public QuestionAndResult(){
        super();
    }  
  
    public int getResultType() {
        return resultType;
    }
    public void setResultType(int resultType) {
        this.resultType = resultType;
    }
    public String getDetailedAnswer() {
        return detailedAnswer;
    }
    public void setDetailedAnswer(String detailedAnswer) {
        this.detailedAnswer = detailedAnswer;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public long getQrid() {
        return qrid;
    }

    public void setQrid(long qrid) {
        this.qrid = qrid;
    }

    public long getQid() {
        return qid;
    }

    public void setQid(long qid) {
        this.qid = qid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        JSONObject questionResult=new JSONObject();
        questionResult.put("qrid", this.getQrid());
        questionResult.put("questionid", this.getQid());
        if(1==this.getResultType())
            questionResult.put("resultType", "Yes");
        else if(2==this.getResultType())
            questionResult.put("resultType", "No");
        questionResult.put("detailedAnswer", this.getDetailedAnswer());
        questionResult.put("content", this.getContent());
        questionResult.put("remark", this.getRemark());
        return questionResult.toString();
    }            
}
