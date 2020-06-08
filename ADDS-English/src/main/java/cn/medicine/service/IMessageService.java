package cn.medicine.service;

import java.util.List;

import cn.medicine.pojo.Message;
import cn.medicine.utils.MyException;

public interface IMessageService {
    
    /**
     * 
     * @Function:     add 
     * @Description:   添加消息 
     *                 <功能详细描述>
     *
     * @param message
     * @return
     */
    public int add(Message message) throws MyException;
    /**
     * 
     * @Function:     get 
     * @Description:   通过id查找消息  
     *                 <功能详细描述>
     *
     * @param id
     * @return
     */
    public Message get(long id) throws MyException;
    /**
     * 
     * @Function:     getByFromandTo 
     * @Description:   查找某用户来自另一用户的消息  
     *                 <功能详细描述>
     *
     * @param from
     * @param to
     * @return
     */
    public List<Message> getByFromandTo(long from,long to) throws MyException;

}
