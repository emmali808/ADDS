package cn.medicine.graph.entity;

public class Line {
    private Long start;
    private Long end;
    private String type;

    public Line(Long start,Long end,String type){
        this.start = start;
        this.end = end;
        this.type = type;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
