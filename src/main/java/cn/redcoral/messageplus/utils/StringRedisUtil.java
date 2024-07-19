package cn.redcoral.messageplus.utils;

import cn.redcoral.messageplus.constant.CachePrefixConstant;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 操作Redis工具类
 * @author mo
 **/
@Service
public class StringRedisUtil {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 存储用户ID及所在服务ID
     * @param id 用户ID
     */
    public void setUserService(String id) {
        stringRedisTemplate.opsForValue().set(CachePrefixConstant.USER_SERVICE_PREFIX + id, MessagePlusProperties.serviceId);
    }
    /**
     * 获取用户ID及所在服务ID
     * @param id 用户ID
     */
    public String getUserService(String id) {
        return stringRedisTemplate.opsForValue().get(CachePrefixConstant.USER_SERVICE_PREFIX + id);
    }


}
