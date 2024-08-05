package cn.redcoral.messageplus.utils.cache.impl;

import cn.redcoral.messageplus.constant.CachePrefixConstant;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import cn.redcoral.messageplus.utils.cache.UserRedisUtil;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 操作Redis工具类
 * @author mo
 **/
@Service
public class UserRedisUtilImpl implements UserRedisUtil {
    @Autowired
    private Cache<String, String> stringCache;

    /**
     * 存储用户ID及所在服务ID
     * @param id 用户ID
     */
    @Override
    public void setUserService(String id) {
        stringCache.put(CachePrefixConstant.USER_MESSAGES_PREFIX + id, MessagePlusProperties.serviceId);
    }
    /**
     * 获取用户ID及所在服务ID
     * @param id 用户ID
     */
    @Override
    public String getUserService(String id) {
        return stringCache.get(CachePrefixConstant.USER_MESSAGES_PREFIX + id, (k)->null);
    }


}
