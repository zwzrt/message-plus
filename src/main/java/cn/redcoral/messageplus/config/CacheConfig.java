package cn.redcoral.messageplus.config;

import cn.redcoral.messageplus.properties.MessagePersistenceProperties;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author mo
 **/
@Configuration
public class CacheConfig {

    public static Cache<String, String> stringCache = Caffeine.newBuilder()
            //初始数量
            .initialCapacity(10)
            //最大条数
            .maximumSize(100)
            //最后一次写操作后经过指定时间过期
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build();

    public static Cache<String, Long> stringLongCache = Caffeine.newBuilder()
            //初始数量
            .initialCapacity(10)
            //最大条数
            .maximumSize(100)
            //expireAfterWrite和expireAfterAccess同时存在时，以expireAfterWrite为准
            //最后一次写操作后经过指定时间过期
            .expireAfterWrite(10, TimeUnit.SECONDS)
            //最后一次读或写操作后经过指定时间过期
//                .expireAfterAccess(1, TimeUnit.SECONDS)
            //监听缓存被移除
//                .removalListener((key, val, removalCause) -> { })
            //记录命中
//                .recordStats()
            .build();
    /**
     * 消息限制缓存
     */
    public static Cache<String, Integer> messageRestrictionsCache = Caffeine.newBuilder()
            .initialCapacity(1000)
            .maximumSize(1000000000)
            //最后一次写操作后经过指定时间过期
            .expireAfterWrite(MessagePersistenceProperties.cycleRestrictionsTime, TimeUnit.SECONDS)
            .build();

    @Bean
    public Cache<String, String> stringCache() {
        return stringCache;
    }

    @Bean
    public Cache<String, Integer> messageRestrictionsCache() {
        return messageRestrictionsCache;
    }

    @Bean
    public Cache<String, Long> stringStringCache() {
        return stringLongCache;
    }

}
