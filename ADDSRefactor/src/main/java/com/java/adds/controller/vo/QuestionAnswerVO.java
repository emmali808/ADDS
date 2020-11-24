package com.java.adds.controller.vo;


public class QuestionAnswerVO {
    private Integer type; //1:选择，2：详细回答
    private String answer; //如果是选择题为“yes” or "no",如果是详细回答题，给出详细回答
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
