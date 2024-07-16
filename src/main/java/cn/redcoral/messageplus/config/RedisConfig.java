package cn.redcoral.messageplus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import cn.redcoral.messageplus.receiver.RedisReceiver;

/**
 * @author mo
 * @日期: 2024-07-15 16:09
 **/
@Configuration
public class RedisConfig {
    private static final String BASE_KEY = "messageplus:";
    public static final String REDIS_TOPIC = BASE_KEY + "newmessage";

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
