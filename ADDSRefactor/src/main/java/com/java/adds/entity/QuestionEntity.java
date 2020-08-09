package com.java.adds.entity;


public class QuestionEntity {
    private Long qid;//问题id
    private String content;  //问题内容
    private Long hospitalDepartmentId;  //属于哪个科室的问题
    private Integer type;     //问题类型  1：单选    2：详细回答问题
    private Long userid;   //问题提出者
    private String remark;//备注
    private Integer answered;  //当前用户有没有回答过，1，回答了，2，未回答

    public Integer getAnswered() {
        return answered;
    }

    public void setAnswered(Integer answered) {
        this.answered = answered;
    }

    public Long getQid() {
        return qid;
    }

    public void setQid(Long qid) {
        this.qid = qid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getHospitalDepartmentId() {
        return hospitalDepartmentId;
    }

    public void setHospitalDepartmentId(Long hospitalDepartmentId) {
        this.hospitalDepartmentId = hospitalDepartmentId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
