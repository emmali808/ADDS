package com.java.adds.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**ljy
 *发送邮件设置
 */
@Component
public class EmailUtil {
    @Autowired
    JavaMailSender mailSender;
    public boolean sendSimpleEmail(String topic,String content,ArrayList<String> recepients)
    {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("addstest@163.com");
            message.setSubject(topic);
            message.setText(content);
            for (int i = 0; i < recepients.size(); i++) {
                message.setTo(recepients.get(i));
                mailSender.send(message);
            }
        }catch (Exception e)
        {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public boolean sendSimpleEmail(String topic,String content,String recepients)
    {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("addstest@163.com");
            message.setSubject(topic);
            message.setText(content);
            message.setTo(recepients);
            mailSender.send(message);
            System.out.println("发送成功");
        }catch (Exception e)
        {
            System.out.println(e);
            return false;
        }
        return true;
    }
}
