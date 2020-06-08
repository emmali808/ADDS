package cn.medicine.websocket;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;


import cn.medicine.Contant.Contant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.medicine.pojo.Message;
import cn.medicine.service.IMessageService;
import cn.medicine.service.IMyDataService;
import cn.medicine.utils.JsonUtil;
/**
 * 
 * @ClassName:MyWebSocketHandler
 * @Description: Socket处理器
 * 
 * @author Goofy
 * @version  
 * @Date	 2016-8-21下午9:17:25
 *
 * @History:// 历史修改记录 
 * <author>  // 修改人
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述修改内容
 */
@Component
public class MyWebSocketHandler implements WebSocketHandler{
    
    private final static Logger logger= LogManager.getLogger(MyWebSocketHandler.class);
    
    public static final Map<Long, WebSocketSession> userSocketSessionMap;
    
    @Resource
    private IMyDataService myDataService;   
    @Resource
    private IMessageService messageService;

    static {
        userSocketSessionMap = new HashMap<Long, WebSocketSession>();
    }

    /**
     * 建立连接后
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        Long uid = (Long) session.getAttributes().get("uid");
        if (userSocketSessionMap.get(uid) == null) {
            userSocketSessionMap.put(uid, session);
        }
        
        Message msg=new Message();
        msg.setFrom(-1l);
        msg.setText("newuser");
        msg.setFromName("broadcast");        
        broadcast(new TextMessage(JsonUtil.toJsonString(msg)));
    }
    /**
     * 消息处理，在客户端通过Websocket API发送的消息会经过这里，然后进行相应的处理
     */
    @Override
    public void handleMessage(WebSocketSession session,
            WebSocketMessage<?> message) throws Exception {
        if(message.getPayloadLength()==0)
            return;
        Message msg=(Message)JsonUtil.toBean(message.getPayload().toString(), Message.class);
        msg.setDate(new Date());
        
        Long to=msg.getTo();
        try{
            messageService.add(msg);
        }catch(Exception e){
            logger.info(e.getMessage());
        }
               
        if(to>0){
            sendMessageToUser(msg.getTo(), new TextMessage(JsonUtil.toJsonString(msg)));
        }else{
            broadcast(new TextMessage(JsonUtil.toJsonString(msg)));
        }
        
        
    }
    /**
     * 消息传输错误处理
     */
    @Override
    public void handleTransportError(WebSocketSession session,
            Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        Iterator<Entry<Long, WebSocketSession>> it = userSocketSessionMap
                .entrySet().iterator();
        // 移除Socket会话
        while (it.hasNext()) {
            Entry<Long, WebSocketSession> entry = it.next();
            if (entry.getValue().getId().equals(session.getId())) {
                userSocketSessionMap.remove(entry.getKey());
                System.out.println("Socket会话已经移除:用户ID" + entry.getKey());
                break;
            }
        }
    }
    /**
     * 关闭连接后
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session,
            CloseStatus closeStatus) throws Exception {
        String sessionid=session.getId();
        boolean flag=false;
        long uid;
        String content;
        JSONArray array = null;
        JSONObject object;
        
        System.out.println("Websocket:" + sessionid + "已经关闭");
        Iterator<Entry<Long, WebSocketSession>> it = userSocketSessionMap
                .entrySet().iterator();
        // 移除Socket会话
        while (it.hasNext()) {
            Entry<Long, WebSocketSession> entry = it.next();
            if (entry.getValue().getId().equals(sessionid)) {
                userSocketSessionMap.remove(entry.getKey());
                System.out.println("Socket会话已经移除:用户ID" + entry.getKey());
                
                uid=entry.getKey();
                content=myDataService.get(Contant.websocketuser);
                if(content!=null){
                    array=JsonUtil.getJSONArrayFromJsonString(content);                  
                    for(int i=0;i<array.size();i++){
                        object=array.getJSONObject(i);
                        if((object.getLong("id")-uid)<0.000001){
                            array.remove(i);
                            flag=true;
//                            break;
                        }
                    }
                    if(flag==true){
                        myDataService.update(Contant.websocketuser, array.toJSONString());
                        flag=false;
                    }
                    
                }
                
                break;
            }
        }                               
    }

    @Override
    public boolean supportsPartialMessages() {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * 给某个用户发送消息
     * 
     * @param userName
     * @param message
     * @throws IOException
     */
    public void sendMessageToUser(Long uid, TextMessage message)
            throws IOException {
        WebSocketSession session = userSocketSessionMap.get(uid);
        if (session != null && session.isOpen()) {
            session.sendMessage(message);
        }
    }
    
    /**
     * 给所有在线用户发送消息
     * 
     * @param message
     * @throws IOException
     */
    public void broadcast(final TextMessage message) throws IOException {
        Iterator<Entry<Long, WebSocketSession>> it = userSocketSessionMap
                .entrySet().iterator();

        // 多线程群发
        while (it.hasNext()) {

            final Entry<Long, WebSocketSession> entry = it.next();

            if (entry.getValue().isOpen()) {
                // entry.getValue().sendMessage(message);
                new Thread(new Runnable() {

                    public void run() {
                        try {
                            if (entry.getValue().isOpen()) {
                                entry.getValue().sendMessage(message);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }).start();
            }

        }
    }

}
