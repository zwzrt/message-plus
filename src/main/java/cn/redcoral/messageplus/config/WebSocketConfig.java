package cn.redcoral.messageplus.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocket
// @EnableWebSocketMessageBroker注解用于开启使用STOMP协议来传输基于代理（MessageBroker）的消息，这时候控制器（controller）
// 开始支持@MessageMapping,就像是使用@requestMapping一样。

//SockJS 协议:优先使用原生WebSocket，如果在不支持websocket的浏览器中，会自动降为轮询的方式。
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //注册一个 Stomp 的节点(endpoint)，指定规则http://IP:PORT/ws,并指定使用 SockJS 协议。
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 广播式配置名为 /nasus 消息代理 , 这个消息代理必须和 controller 中的 @SendTo 配置的地址前缀一样或者全匹配
        // 客户端订阅地址的前缀信息
        registry.enableSimpleBroker("/topic");

        // 设置stomp的endpoint前缀
//        registry.enableStompBrokerRelay("/topic", "/queue");

        // 设置哪些目的地前缀用于代理目的
        //服务端接收地址的前缀
        //客户端发送到以"/app"开头的目的地的消息会被应用所接收处理。
        registry.setApplicationDestinationPrefixes("/app");
    }
}

