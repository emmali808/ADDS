package cn.medicine.websocket;

import javax.annotation.Resource;


import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 
 * @ClassName:WebSocketConfig
 * WebScoket配置处理器
 * @author Goofy
 * @version  
 * @Date	 2016-8-21下午9:28:16
 *
 * @History:// 历史修改记录 
 * <author>  // 修改人
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述修改内容
 */
@Component
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
    @Resource
    MyWebSocketHandler handler;
    
    @Resource
    HandShake handShake;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(handler, "/ws").addInterceptors(new HandShake());
//
//        registry.addHandler(handler, "/ws/sockjs").addInterceptors(new HandShake()).withSockJS();
        
        registry.addHandler(handler, "/ws").addInterceptors(handShake);

        registry.addHandler(handler, "/ws/sockjs").addInterceptors(handShake).withSockJS();
    }

}
