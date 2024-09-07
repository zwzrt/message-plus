package cn.redcoral.messageplus.interceptor;

import cn.redcoral.messageplus.manage.SystemManage;
import cn.redcoral.messageplus.port.MessagePlusUtil;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * message-plus拦截器，全部放行
 */
@Configuration
public class SystemInterceptor implements HandlerInterceptor {

    @Autowired
    private SystemManage systemManage;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 获取token值
        String token = request.getHeader("token");
        // 未登录，拒绝处理请求
        if (token == null || systemManage.get(token)==null) {
            response.sendError(401);
            return false;
        }
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {}
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {}
}
