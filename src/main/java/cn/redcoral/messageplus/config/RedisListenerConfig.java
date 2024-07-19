package cn.redcoral.messageplus.config;

import cn.redcoral.messageplus.constant.CachePrefixConstant;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import cn.redcoral.messageplus.receiver.RedisReceiver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @author mo
 **/
@Configuration
public class RedisListenerConfig {
    public static final String REDIS_TOPIC = CachePrefixConstant.PREFIX_HEAD + "newmessage";


    @Bean
    public MessageListenerAdapter messageListenerAdapterByAlone(RedisReceiver redisReceiver) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(redisReceiver,
                "onMessage");
        return messageListenerAdapter;
    }
    @Bean
    public MessageListenerAdapter messageListenerAdapter(RedisReceiver redisReceiver){
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(redisReceiver,
                "onMessage");
        return messageListenerAdapter;
    }
    @Bean
    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory factory, RedisReceiver redisReceiver){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(messageListenerAdapterByAlone(redisReceiver), new PatternTopic(REDIS_TOPIC+":"+ MessagePlusProperties.serviceId));
        container.addMessageListener(messageListenerAdapter(redisReceiver), new PatternTopic(REDIS_TOPIC));
        return container;
    }
}
