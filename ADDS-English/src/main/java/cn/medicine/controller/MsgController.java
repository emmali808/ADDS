package cn.medicine.controller;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.TextMessage;

import cn.medicine.pojo.Message;
import cn.medicine.utils.JsonUtil;
import cn.medicine.websocket.MyWebSocketHandler;


@Controller
@RequestMapping("/msg")
public class MsgController {
    
    @Resource
    MyWebSocketHandler handler;
    
 // 跳转到交谈聊天页面
    @RequestMapping(value = "talk", method = RequestMethod.GET)
    public ModelAndView talk() {
        return new ModelAndView("talk");
    }

    // 跳转到发布广播页面
    @RequestMapping(value = "broadcast", method = RequestMethod.GET)
    public ModelAndView broadcast() {
        return new ModelAndView("broadcast");
    }

    // 发布系统广播（群发）
    @ResponseBody
    @RequestMapping(value = "broadcast", method = RequestMethod.POST)
    public void broadcast(String text) throws IOException {
        Message msg = new Message();
        msg.setDate(new Date());
        msg.setFrom(-1L);
        msg.setFromName("系统广播");
        msg.setTo(0L);
        msg.setText(text);
        handler.broadcast(new TextMessage(JsonUtil.toJsonString(msg)));
    }


}
