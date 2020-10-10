package com.java.adds.entity;


public class DetailAnswerEntity {
    private Long qrid;
    private Long questionid;
    private Long userid;
    private String detailedAnswer;
    private String remark;

    public Long getQrid() {
        return qrid;
    }

    public void setQrid(Long qrid) {
        this.qrid = qrid;
    }

    public Long getQuestionid() {
        return questionid;
    }

    public void setQuestionid(Long questionid) {
        this.questionid = questionid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
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
}
