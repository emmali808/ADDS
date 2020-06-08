package cn.medicine.dao;

import java.util.List;

import cn.medicine.pojo.MessageTemp;

public interface MessageMapper {
    /**
     * 
     * @Function:     add 
     * @Description:   添加消息 
     *                 <功能详细描述>
     *
     * @param message
     * @return
     */
    public int add(MessageTemp message);
    /**
     * 
     * @Function:     get 
     * @Description:   通过id查找消息  
     *                 <功能详细描述>
     *
     * @param id
     * @return
     */
    public MessageTemp get(long id);
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
    public List<MessageTemp> getByFromandTo(long from,long to);


}
