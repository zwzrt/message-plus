package cn.redcoral.messageplus.config;

import cn.redcoral.messageplus.initialize.MessageInitialize;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * message-plus的普通配置类
 * @author mo
 **/
@Slf4j
@EnableCaching//开启基于注解的缓存支持
@ComponentScan({"cn.redcoral.messageplus"})
@MapperScan("cn.redcoral.messageplus.data.mapper")
public class MessagePlusConfig {

    /**
     * 	注入ServerEndpointExporter，
     * 	这个bean会自动注册使用了@ServerEndpoint注解声明的Websocket endpoint
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * 初始化消息服务
     * @return MessageInitialize的实例
     */
    @Bean
    public MessageInitialize messageInitialize() {
        log.info("MessagePlus is enabled...");
        log.info("MessagePlus ServiceId: {}", MessagePlusProperties.getServiceId());
        return new MessageInitialize();
    }

}
