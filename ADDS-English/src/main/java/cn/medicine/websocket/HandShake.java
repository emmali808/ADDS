package cn.medicine.websocket;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import cn.medicine.Contant.Contant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.medicine.pojo.User;
import cn.medicine.service.IMyDataService;
import cn.medicine.utils.JsonUtil;
/**
 * 
 * @ClassName:HandShake
 * @Description: Socket建立连接（握手）和断开
 * @Function List:TODO 主要函数及其功能
 *
 * @author   ztxu
 * @version  
 * @Date	 2016-8-21下午9:15:57
 *
 * @History:// 历史修改记录 
 * <author>  // 修改人
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述修改内容
 */
@Component
public class HandShake implements HandshakeInterceptor{
    
    @Resource
    private IMyDataService myDataService;    

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
            ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        
        System.out.println("Websocket: into beforeHandshake method");
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession(false);
            // 标记用户
           // Long uid = (Long) session.getAttribute("uid");
            User user=(User)session.getAttribute("user");
            Long uid=user.getId();
            
            /*Map<Long,String> allOnlineUser;
            ServletContext sc= session.getServletContext();
            allOnlineUser=(Map<Long,String>)sc.getAttribute("onlineUser");//所有在线的用户
            if(allOnlineUser==null){
                allOnlineUser=new HashMap<Long,String>();
            }
            allOnlineUser.put(user.getId(), user.getUsername());
            sc.setAttribute("onlineUser", allOnlineUser);*/
            
            if(uid!=null){
                attributes.put("uid", uid);
                System.out.println("Websocket:用户[ID:" + uid + "]已经建立连接");
                
                String content=myDataService.get(Contant.websocketuser);
                System.out.println("content"+content);
                if(!content.trim().equals("")){
                    JSONArray array=JsonUtil.getJSONArrayFromJsonString(content);
                    JSONObject object=new JSONObject();
                    object.put("id", uid);
                    object.put("name", user.getUsername());
                    object.put("type", user.getType());
                    array.add(object);
                    
                    myDataService.update(Contant.websocketuser,array.toJSONString());
                }else{
                    JSONArray array=new JSONArray();
                    JSONObject object=new JSONObject();
                    object.put("id", uid);
                    object.put("name", user.getUsername());
                    object.put("type", user.getType());
                    array.add(object);
                    System.out.println("array:"+array.toJSONString()+"name:"+user.getUsername());
                    myDataService.add(Contant.websocketuser,array.toJSONString());
                }
                
            }else{
                return false;
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
            ServerHttpResponse response, WebSocketHandler wsHandler,
            Exception exception) {
       /* if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession(false);
            User user=(User)session.getAttribute("user");
            Long uid=user.getId();
            boolean flag=false;
            
            if(uid!=null){
                String content=myDataService.get(Contant.websocketuser);
                JSONArray array = null;
                if(content!=null){
                    array=JsonUtil.getJSONArrayFromJsonString(content);
                    JSONObject object=new JSONObject();
                    for(int i=0;i<array.size();i++){
                        object=array.getJSONObject(i);
                        if(object.getLong("id")==uid){
                            array.remove(i);
                            flag=true;
                            break;
                        }
                    }
                    
                }
                if(flag==true){
                    myDataService.update(Contant.websocketuser, array.toJSONString());
                }
            }
            
      
        }*/
        
        
                
        /*ServletContext sc= session.getServletContext();
        Map<Long,String> allOnlineUser=(Map<Long,String>)sc.getAttribute("onlineUser");//所有在线的用户
        User user=(User)session.getAttribute("user");
        if(allOnlineUser!=null&&allOnlineUser.size()>0){
            allOnlineUser.remove(user.getId());
            sc.setAttribute("onlineUser", allOnlineUser);
        }*/               
    }

}
