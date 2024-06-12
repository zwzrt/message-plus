package cn.redcoral.messageplus.config;

import cn.redcoral.messageplus.utils.ChatUtils;
import cn.redcoral.messageplus.utils.SpringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author mo
 * @Description:
 * @日期: 2024-05-25 14:01
 **/
@EnableAspectJAutoProxy // 声明有注解AOP开发
public class SpringConfig {
    /**
     * 	注入ServerEndpointExporter，
     * 	这个bean会自动注册使用了@ServerEndpoint注解声明的Websocket endpoint
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
