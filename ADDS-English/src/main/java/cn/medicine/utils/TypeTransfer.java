package cn.medicine.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.medicine.pojo.Message;
import cn.medicine.pojo.MessageTemp;

public class TypeTransfer {
    
    public static MessageTemp MessageToMessageTemp(Message message){
        MessageTemp temp=new MessageTemp();
        temp.setId(message.getId());
        temp.setFrom(message.getFrom());
        temp.setFromName(message.getFromName());
        temp.setTo(message.getTo());
        temp.setText(message.getText());
        temp.setDatetext(DateUtil.makeDateToString(message.getDate()));
        return temp;
    }
    
    public static Message MessageTempToMessage(MessageTemp temp){
        Message message=new Message();
        message.setId(temp.getId());
        message.setFrom(temp.getFrom());
        message.setFromName(temp.getFromName());
        message.setTo(temp.getTo());
        message.setText(temp.getText());
        message.setDate(DateUtil.makeStringToDate(temp.getDatetext()));
        return message;
    }
    
    public static List<Message> MessageTempListToMessageList(List<MessageTemp> temps){
        List<Message> result=new ArrayList<Message>();
        Iterator<MessageTemp> it=temps.iterator();   
        Message message;
        while(it.hasNext()){
            message=MessageTempToMessage(it.next());
            result.add(message);
        }
        return result;
    }

}
