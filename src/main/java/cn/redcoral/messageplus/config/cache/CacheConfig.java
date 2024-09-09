package cn.redcoral.messageplus.config.cache;

import cn.redcoral.messageplus.data.entity.ChatRoom;
import cn.redcoral.messageplus.data.entity.po.HistoryMessagePo;
import cn.redcoral.messageplus.properties.MessagePersistenceProperties;
import cn.redcoral.messageplus.utils.cache.MPCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存配置类，用于创建和配置应用中使用的缓存实例
 * @author mo
 */
@Configuration
public class CacheConfig {

    /**
     * 字符串缓存实例，用于缓存字符串对字符串的数据
     */
    public static Cache<String, String> stringCache = Caffeine.newBuilder()
            // 初始容量设置为10，以减少初始时的资源分配
            .initialCapacity(10)
            // 最大容量设置为100，以限制内存使用
            .maximumSize(100)
            // 设置写入后10秒过期，确保数据的时效性
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build();

    /**
     * 字符串-长整型缓存实例，用于缓存字符串对长整型的数据
     */
    public static Cache<String, Long> stringLongCache = Caffeine.newBuilder()
            // 初始容量设置为10，以减少初始时的资源分配
            .initialCapacity(10)
            // 最大容量设置为100，以限制内存使用
            .maximumSize(100)
            // 设置写入后10秒过期，确保数据的时效性
            .expireAfterWrite(10, TimeUnit.SECONDS)
            // 注释部分为其他可选配置，如读取或写入后过期、移除监听和记录统计信息等
            // 最后一次读或写操作后经过指定时间过期
//                .expireAfterAccess(1, TimeUnit.SECONDS)
            // 监听缓存被移除
//                .removalListener((key, val, removalCause) -> { })
            // 记录命中
//                .recordStats()
            .build();

    /**
     * 字符串-长整型缓存实例，用于缓存字符串对长整型的数据
     */
    public static Cache<String, ChatRoom> stringChatRoomCache = Caffeine.newBuilder()
            // 初始容量设置为10，以减少初始时的资源分配
            .initialCapacity(10)
            // 最大容量设置为100，以限制内存使用
            .maximumSize(100)
            // 设置写入后10秒过期，确保数据的时效性
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build();

    /**
     * 消息限制缓存，用于存储和管理消息的发送限制信息
     */
    public static Cache<String, Integer> stringIntCache = Caffeine.newBuilder()
            .initialCapacity(1000) // 初始容量设置为1000，适应较大的初始需求
            .maximumSize(1000000000) // 设置一个较大的最大容量以适应大量消息限制数据
            // 根据消息持久化属性中的周期限制时间设置过期时间，确保与业务逻辑一致
            .expireAfterWrite(MessagePersistenceProperties.cycleRestrictionsTime, TimeUnit.SECONDS)
            .build();

    public static Cache<String, List<HistoryMessagePo>> messageQueueCache = Caffeine.newBuilder()
            .initialCapacity(100) // 初始容量设置为1000，适应较大的初始需求
            .maximumSize(10000000) // 设置一个较大的最大容量以适应大量消息限制数据
            // 根据消息持久化属性中的周期限制时间设置过期时间，确保与业务逻辑一致
            .expireAfterWrite(MessagePersistenceProperties.messageTimeOut, TimeUnit.MINUTES)
            .build();
    
    /**
     * 配置字符串缓存Bean
     * @return CacheString, String 字符串缓存实例
     */
    @Bean
    public MPCache<String, String> stringCache() {
        return new MPCache<>(stringCache);
    }

    /**
     * 配置消息限制缓存Bean
     * @return Cache 消息限制缓存实例
     */
    @Bean
    public MPCache<String, Integer> stringIntCache() {
        return new MPCache<>(stringIntCache);
    }

    /**
     * 配置消息限制缓存Bean
     * @return Cache 消息限制缓存实例
     */
    @Bean
    public MPCache<String, ChatRoom> stringChatRoomCache() {
        return new MPCache<>(stringChatRoomCache);
    }

    /**
     * 配置字符串-长整型缓存Bean
     * @return Cache字符串-长整型缓存实例
     */
    @Bean
    public MPCache<String, Long> stringStringCache() {
        return new MPCache<>(stringLongCache);
    }
    
    @Bean
    public MPCache<String, List<HistoryMessagePo>> messageListCache() {
        return new MPCache<String, List<HistoryMessagePo>>(messageQueueCache);
    }
}
