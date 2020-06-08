package cn.medicine.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.medicine.dao.IMessageDao;
import cn.medicine.dao.MessageMapper;
import cn.medicine.pojo.Message;
import cn.medicine.pojo.MessageTemp;
import cn.medicine.utils.JsonUtil;
import cn.medicine.utils.MyException;
import cn.medicine.utils.TypeTransfer;

public class MessageDaoImpl implements IMessageDao{
    
    private final static Logger logger= LogManager.getLogger(MessageDaoImpl.class);
    
    private boolean isDebugEnable=logger.isDebugEnabled();

    private boolean isErrorEnable=logger.isErrorEnabled();
    
    private MessageMapper messageMapper;
    
    public MessageDaoImpl(){
        super();
    }
    
    public MessageDaoImpl(MessageMapper messageMapper){
        super();
        this.messageMapper=messageMapper;
    }

    @Override
    public int add(Message message) throws MyException {
        try{
            MessageTemp mtemp=TypeTransfer.MessageToMessageTemp(message);
            logger.info(JsonUtil.toJsonString(mtemp));
            
            messageMapper.add(mtemp);
            return 1;
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("添加异常("+message.toString()+"):"+e.getMessage());
            throw new MyException("添加异常("+message.toString()+"):"+e.getMessage());
        }
    }

    @Override
    public Message get(long id) throws MyException {
        try{
            MessageTemp temp=messageMapper.get(id);
            Message message=TypeTransfer.MessageTempToMessage(temp);
            return message;
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("查询异常(id="+id+"):"+e.getMessage());
            throw new MyException("查询异常(id="+id+"):"+e.getMessage());
        }
    }

    @Override
    public List<Message> getByFromandTo(long from, long to) throws MyException {
        try{
            List<MessageTemp> temps=messageMapper.getByFromandTo(from, to);
            
            return TypeTransfer.MessageTempListToMessageList(temps);
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("查询异常(from="+from+",to="+to+"):"+e.getMessage());
            throw new MyException("查询异常(from="+from+",to="+to+"):"+e.getMessage());
        }
    }

}
