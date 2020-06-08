package cn.medicine.pojo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;

public class MessageTemp implements Serializable{

    
    private static final long serialVersionUID = 1L;
    
    private long id;
    
    //发送者
    private Long from;
    //发送者名称
    private String fromName;
    //接收者
    private Long to;
    //发送的文本
    private String text;
    //发送日期
    private String datetext;
    
    public MessageTemp(){
        super();
    }
    
    public MessageTemp(Long from,String fromName,Long to,String text,String datetext){
        super();
        this.from=from;
        this.fromName=fromName;
        this.to=to;
        this.text=text;
        this.datetext=datetext;
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

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
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

    

    public String getDatetext() {
        return datetext;
    }

    public void setDatetext(String datetext) {
        this.datetext = datetext;
    }

    @Override
    public String toString() {
        JSONObject object=new JSONObject();
        object.put("id", this.getId());
        object.put("from", this.getFrom());
        object.put("fromName", this.getFromName());
        object.put("to", this.getTo());
        object.put("text", this.getText());
        object.put("datetext", this.getDatetext());
        return object.toJSONString();
    }
    
    

}
