package cn.redcoral.messageplus.config;

import cn.redcoral.messageplus.utils.CachePrefixConstant;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import cn.redcoral.messageplus.receiver.RedisReceiver;

import java.util.concurrent.TimeUnit;

/**
 * @author mo
 **/
@Configuration
public class CacheConfig {

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
            .build();;

    @Bean
    public Cache<String, Long> stringStringCache() {
        return stringLongCache;
    }


    public static final String REDIS_TOPIC = CachePrefixConstant.PREFIX_HEAD + "newmessage";

    @Bean
    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory factory,
                                                           MessageListenerAdapter messageListenerAdapter){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(messageListenerAdapter, new PatternTopic(REDIS_TOPIC));
        return container;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(RedisReceiver redisReceiver){
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(redisReceiver,
                "onMessage");
        return messageListenerAdapter;
    }

}
