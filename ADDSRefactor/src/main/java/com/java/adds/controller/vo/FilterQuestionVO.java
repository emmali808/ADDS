package com.java.adds.controller.vo;



public class FilterQuestionVO {
    private Integer answeredOrNot;  //1:已经回答，2：还未回答
    private Integer questionType;  //1:选择题，2：详细解答题
    private Integer start;  //分页查询的页码
    private Integer limit;  //每页显示的记录数

    public Integer getAnsweredOrNot() {
        return answeredOrNot;
    }

    public void setAnsweredOrNot(Integer answeredOrNot) {
        this.answeredOrNot = answeredOrNot;
    }

    public Integer getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
