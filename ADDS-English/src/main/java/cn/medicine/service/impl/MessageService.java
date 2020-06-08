package cn.medicine.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.medicine.dao.IMessageDao;
import cn.medicine.pojo.Message;
import cn.medicine.service.IMessageService;
import cn.medicine.utils.MyException;
@Service
public class MessageService implements IMessageService{
    
    @Autowired
    @Qualifier("messageDao")
    private IMessageDao messageDao;

    @Override
    public int add(Message message) throws MyException {
       return messageDao.add(message);
    }

    @Override
    public Message get(long id) throws MyException {
        return messageDao.get(id);
    }

    @Override
    public List<Message> getByFromandTo(long from, long to) throws MyException {
        
        return messageDao.getByFromandTo(from, to);
    }

}
