package cn.redcoral.messageplus.config;

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
 * @日期: 2024-05-25 14:01
 **/
@Slf4j
@EnableAspectJAutoProxy // 声明有注解AOP开发
@Import({MessagePlusProperties.class})
public class MessagePlusConfig {
    /**
     * 	注入ServerEndpointExporter，
     * 	这个bean会自动注册使用了@ServerEndpoint注解声明的Websocket endpoint
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Resource
    private MessagePlusProperties messagePlusProperties;
    @Bean
    public MessageInitialize messageInitialize() {
        log.info("MessagePlus is enabled...");
        log.info("ServiceId: {}", messagePlusProperties.getServiceId());
        return new MessageInitialize();
    }

}
