package cn.redcoral.messageplus.config.cluster;

import cn.redcoral.messageplus.data.entity.vo.ChatRoom;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * @author mo
 **/
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Long> stringLongRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
    @Bean
    public RedisTemplate<String, ChatRoom> stringChatRoomRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, ChatRoom> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatRoom.class));
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(ChatRoom.class));
        return redisTemplate;
    }
}
