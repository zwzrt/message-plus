package cn.redcoral.messageplus.config;

import cn.redcoral.messageplus.controller.MessagePlusController;
import cn.redcoral.messageplus.initialize.MessageInitialize;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.annotation.Resource;

/**
 * message-plus的普通配置类
 * @author mo
 **/
@Slf4j
@EnableAspectJAutoProxy // 声明有注解AOP开发
@Import({MessagePlusController.class})
public class MessagePlusConfig {
    /**
     * 	注入ServerEndpointExporter，
     * 	这个bean会自动注册使用了@ServerEndpoint注解声明的Websocket endpoint
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public MessageInitialize messageInitialize() {
        log.info("MessagePlus is enabled...");
        log.info("ServiceId: {}", MessagePlusProperties.getServiceId());
        return new MessageInitialize();
    }

}
