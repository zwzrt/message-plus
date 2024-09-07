package cn.redcoral.messageplus.config;

import cn.redcoral.messageplus.interceptor.ControllerInterceptor;
import cn.redcoral.messageplus.interceptor.SystemInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类，用于配置与Web相关的组件和行为
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ControllerInterceptor controllerInterceptor;
    @Autowired
    private SystemInterceptor systemInterceptor;

    /**
     * 配置拦截器的路径规则
     *
     * @param registry 拦截器注册对象，用于向Spring MVC注册自定义拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册controllerInterceptor拦截器，并指定其拦截的路径
        registry.addInterceptor(controllerInterceptor).addPathPatterns(
                "/messageplus/single/**",
                "/messageplus/system/send",
                "/messageplus/group/**",
                "/messageplus/chatroom/**"
        ).excludePathPatterns(
                "/messageplus/group/num",
                "/messageplus/chatroom/num"
        );
        // 注册SystemInterceptor拦截器
        registry.addInterceptor(systemInterceptor).addPathPatterns(
                "/messageplus/system/**",
                "/messageplus/group/num",
                "/messageplus/chatroom/num"
        ).excludePathPatterns(
                "/messageplus/system/send",
                "/messageplus/system/login",
                "/messageplus/system/logout",
                "/messageplus/system/isOnLine"
        );
    }
}
