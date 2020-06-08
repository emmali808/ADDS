package cn.medicine.pojo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;

public class Message implements Serializable{

    private static final long serialVersionUID = 2858553997787135034L;
    
    private long id;
    
    //发送者
    public Long from;
    //发送者名称
    public String fromName;
    //接收者
    public Long to;
    //发送的文本
    public String text;
    //发送日期

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date date;
    
    public Message(){
        super();
    }
    
    public Message(Long from,String fromName,Long to,String text,Date date){
        super();
        this.from=from;
        this.fromName=fromName;
        this.to=to;
        this.text=text;
        this.date=date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        JSONObject object=new JSONObject();
        object.put("id", this.getId());
        object.put("from", this.getFrom());
        object.put("fromName", this.getFromName());
        object.put("to", this.getTo());
        object.put("text", this.getText());
        object.put("date", this.getDate());
        return object.toJSONString();
    }
    
    

}
