package cn.medicine.pojo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
/**
 * 
 * @ClassName:QuestionResult
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
public class QuestionResult implements Serializable{

    private static final long serialVersionUID = -3870290454240738609L;
    private long qrid;//id
    private long questionid;//问题的id
    private long userid;//回答问题的用户
    private int resultType;//单选题的回答，1：Yes  2：No
    private String detailedAnswer;//详细回答答案
    private String remark;//备注
    
    public QuestionResult(){
        super();
    }
    public QuestionResult(long questionid,long userid,int resultType){
        super();
        this.questionid=questionid;
        this.userid=userid;
        this.resultType=resultType;        
    }
    public QuestionResult(long qrid,long questionid,long userid,int resultType){
        super();
        this.qrid=qrid;
        this.questionid=questionid;
        this.userid=userid;
        this.resultType=resultType;        
    }
    public QuestionResult(long questionid,long userid,String detailedAnswer){
        super();
        this.questionid=questionid;
        this.userid=userid;
        this.detailedAnswer=detailedAnswer;   
    }
    public QuestionResult(long qrid,long questionid,long userid,String detailedAnswer){
        super();
        this.qrid=qrid;
        this.questionid=questionid;
        this.userid=userid;
        this.detailedAnswer=detailedAnswer;   
    }
    public QuestionResult(long questionid,long userid,int resultType,String remark){
        super();
        this.questionid=questionid;
        this.userid=userid;
        this.resultType=resultType; 
        this.remark=remark;
    }
    public QuestionResult(long qrid,long questionid,long userid,int resultType,String remark){
        super();
        this.qrid=qrid;
        this.questionid=questionid;
        this.userid=userid;
        this.resultType=resultType; 
        this.remark=remark;
    }
    public QuestionResult(long questionid,long userid,String detailedAnswer,String remark){
        super();
        this.questionid=questionid;
        this.userid=userid;
        this.detailedAnswer=detailedAnswer;
        this.remark=remark;
    }
    public QuestionResult(long qrid,long questionid,long userid,String detailedAnswer,String remark){
        super();
        this.qrid=qrid;
        this.questionid=questionid;
        this.userid=userid;
        this.detailedAnswer=detailedAnswer;
        this.remark=remark;
    }
    public long getQrid() {
        return qrid;
    }
    public void setQrid(long qrid) {
        this.qrid = qrid;
    }
    public long getQuestionid() {
        return questionid;
    }
    public void setQuestionid(long questionid) {
        this.questionid = questionid;
    }
    public long getUserid() {
        return userid;
    }
    public void setUserid(long userid) {
        this.userid = userid;
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
    @Override
    public String toString() {
        JSONObject questionResult=new JSONObject();
        questionResult.put("qrid", this.getQrid());
        questionResult.put("questionid", this.getQuestionid());
        questionResult.put("userid", this.getUserid());
        if(1==this.getResultType())
            questionResult.put("resultType", "Yes");
        else if(2==this.getResultType())
            questionResult.put("resultType", "No");
        questionResult.put("detailedAnswer", this.getDetailedAnswer());
        questionResult.put("remark", this.getRemark());
        return questionResult.toString();
    }            
}
