package cn.redcoral.messageplus.config;

import cn.redcoral.messageplus.interceptor.ControllerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类，用于配置与Web相关的组件和行为
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 创建并配置ControllerInterceptor拦截器
     *
     * @return ControllerInterceptor拦截器实例
     */
    @Bean
    public ControllerInterceptor controllerInterceptor() {
        return new ControllerInterceptor();
    }

    /**
     * 配置拦截器的路径规则
     *
     * @param registry 拦截器注册对象，用于向Spring MVC注册自定义拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册controllerInterceptor拦截器，并指定其拦截的路径
        registry.addInterceptor(controllerInterceptor()).addPathPatterns(
                "/messageplus/send/single",  // 单个消息发送
                "/messageplus/send/mass",    // 批量消息发送
                "/messageplus/send/system",  // 系统消息发送
                "/messageplus/send/chatroom" // 群组消息发送
        );
    }
}
