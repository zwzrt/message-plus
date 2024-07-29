package cn.redcoral.messageplus.config;

import cn.redcoral.messageplus.constant.CachePrefixConstant;
import cn.redcoral.messageplus.entity.Message;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import cn.redcoral.messageplus.receiver.RedisReceiver;
import cn.redcoral.messageplus.utils.MessagePlusUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.Arrays;

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
    /**
     * 聊天室消息广播
     */
    @Bean
    public MessageListenerAdapter chatRoomMessageBroadcast(){
        return new MessageListenerAdapter(
                (MessageListener) (message, pattern) -> {
                    String json = Arrays.toString(message.getBody());
                    Message msg = JSON.parseObject(json, Message.class);
                    // 调用方法
                    MessagePlusUtils.hasNewMessageByChatRoom(msg);
                }, "onMessage");
    }
    @Bean
    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory factory, RedisReceiver redisReceiver){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(messageListenerAdapterByAlone(redisReceiver), new PatternTopic(REDIS_TOPIC+":"+ MessagePlusProperties.serviceId));
        container.addMessageListener(messageListenerAdapter(redisReceiver), new PatternTopic(REDIS_TOPIC));
        // 广播消息
        container.addMessageListener(chatRoomMessageBroadcast(), new PatternTopic(REDIS_TOPIC+":chatroom"));
        return container;
    }
}
