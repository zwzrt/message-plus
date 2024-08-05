package cn.redcoral.messageplus.utils.cache.impl;

import cn.redcoral.messageplus.constant.CachePrefixConstant;
import cn.redcoral.messageplus.utils.CounterIdentifierUtil;
import cn.redcoral.messageplus.utils.cache.ChatRoomRedisUtil;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 操作ChatRoom的Redis工具类
 * @author mo
 **/
@Service
public class ChatRoomRedisUtilImpl implements ChatRoomRedisUtil {

    @Autowired
    private Cache<String, String> stringCache;

    /**
     * 删除chatRoom缓存
     * @param chatRoomId 聊天室ID
     */
    @Override
    public boolean deleteChatRoomById(String chatRoomId) {
        // 删除聊天室信息
        stringCache.invalidate(CachePrefixConstant.CHAT_ROOM + chatRoomId);
        // 删除聊天室点赞
        stringCache.invalidate(CachePrefixConstant.CHAT_ROOM_THUMBS_UP + chatRoomId);
        return true;
    }

    /**
     * 是否存在
     * @param chatRoomId 聊天室ID
     */
    @Override
    public boolean existence(String chatRoomId) {
        return stringCache.get(CachePrefixConstant.CHAT_ROOM + chatRoomId, (k)->null) != null;
    }

    /**
     * 点赞
     * @param chatRoomId 聊天室ID
     */
    @Override
    public boolean thumbUpChatRoomById(String chatRoomId) {
        // 计数器加一
        CounterIdentifierUtil.numberOfSendsIncrease(CachePrefixConstant.CHAT_ROOM_THUMBS_UP + chatRoomId);
        return true;
    }


}
