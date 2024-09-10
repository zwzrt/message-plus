package cn.redcoral.messageplus.utils.cache.impl;

import cn.redcoral.messageplus.constant.CachePrefixConstant;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import cn.redcoral.messageplus.utils.cache.MPCache;
import cn.redcoral.messageplus.utils.cache.UserCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

import static cn.redcoral.messageplus.config.cache.CacheConfig.stringBooleanCache;
import static cn.redcoral.messageplus.constant.UserCacheConstant.BLACK_PREFIX;

/**
 * 操作cache工具类
 * @author mo
 **/
@Service
public class UserCacheUtilImpl implements UserCacheUtil {
    @Autowired
    private MPCache<String, String> stringCache;

    /**
     * 存储用户ID及所在服务ID
     * @param id 用户ID
     */
    @Override
    public void setUserService(String id) {
        stringCache.put(CachePrefixConstant.USER_MESSAGES_PREFIX + id, MessagePlusProperties.serviceId);
    }

    /**
     * 存储是否被拉黑
     * @param id 拉黑用户
     * @param blackId 被拉黑用户
     * @param isBlack 是否拉黑
     */
    @Override
    public void setIsBlack(String id, String blackId, boolean isBlack) {
        // 删除
        stringBooleanCache.invalidate(BLACK_PREFIX + id + ":" + blackId);
        // 添加
        stringBooleanCache.put(BLACK_PREFIX + id + ":" + blackId, isBlack);
    }

    /**
     * 获取用户ID及所在服务ID
     * @param id 用户ID
     */
    @Override
    public String getUserService(String id) {
        return stringCache.get(CachePrefixConstant.USER_MESSAGES_PREFIX + id, (k)->null);
    }

    /**
     * 查询是否被拉黑
     * @param id 拉黑用户ID
     * @param blackId 被拉黑用户ID
     * @param function 不存在的后续方法
     * @return 是否
     */
    @Override
    public Boolean getIsBlack(String id, String blackId, Function<? super String, ? extends Boolean> function) {
        return stringBooleanCache.get(BLACK_PREFIX +id+":"+blackId, function);
    }
}
