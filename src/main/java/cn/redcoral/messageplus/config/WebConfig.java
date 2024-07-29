package cn.redcoral.messageplus.config;

import cn.redcoral.messageplus.interceptor.ControllerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public ControllerInterceptor controllerInterceptor() {
        return new ControllerInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(controllerInterceptor()).addPathPatterns(
                "/messageplus/send/single",
                "/messageplus/send/mass",
                "/messageplus/send/system",
                "/messageplus/send/chatroom"
        );
    }
}
