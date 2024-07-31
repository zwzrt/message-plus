package cn.redcoral.messageplus.redis.utils;

import cn.redcoral.messageplus.constant.CachePrefixConstant;
import cn.redcoral.messageplus.data.entity.vo.ChatRoom;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 操作ChatRoom的Redis工具类
 * @author mo
 **/
@Service
public class ChatRoomRedisUtil {
    @Autowired
    private RedisTemplate<String, ChatRoom> chatRoomRedisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, Long> stringLongRedisTemplate;

    /**
     * 删除chatRoom缓存
     * @param chatRoomId 聊天室ID
     */
    public boolean deleteChatRoomById(String chatRoomId) {
        // 删除聊天室信息
        Boolean bo = chatRoomRedisTemplate.delete(CachePrefixConstant.CHAT_ROOM + chatRoomId);
        if (Boolean.FALSE.equals(bo)) return bo;
        // 删除聊天室点赞
        bo = stringLongRedisTemplate.delete(CachePrefixConstant.CHAT_ROOM_THUMBS_UP + chatRoomId);
        return Boolean.TRUE.equals(bo);
    }


}
