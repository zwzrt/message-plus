package cn.redcoral.messageplus.config;

import cn.hutool.core.lang.Snowflake;
import cn.redcoral.messageplus.controller.MessagePlusChatRoomController;
import cn.redcoral.messageplus.controller.MessagePlusSendController;
import cn.redcoral.messageplus.initialize.MessageInitialize;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * message-plus的普通配置类
 * @author mo
 **/
@Slf4j
@EnableAspectJAutoProxy // 声明有注解AOP开发
@EnableCaching
@ComponentScan({"cn.redcoral.messageplus.utils.cache.impl"})
//@ServletComponentScan({"cn.redcoral.messageplus.utils.cache"})
@Import({MessageInitialize.class,
        MessagePlusSendController.class, MessagePlusChatRoomController.class, CacheConfig.class})
public class MessagePlusConfig {
    public static Snowflake snowflake = new Snowflake(3, 11);

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