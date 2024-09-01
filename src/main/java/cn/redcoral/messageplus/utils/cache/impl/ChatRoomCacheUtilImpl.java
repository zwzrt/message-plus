package cn.redcoral.messageplus.utils.cache.impl;

import cn.redcoral.messageplus.constant.CachePrefixConstant;
import cn.redcoral.messageplus.data.entity.ChatRoom;
import cn.redcoral.messageplus.utils.CounterIdentifierUtil;
import cn.redcoral.messageplus.utils.cache.ChatRoomCacheUtil;
import cn.redcoral.messageplus.utils.cache.MPCache;
import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static cn.redcoral.messageplus.constant.CachePrefixConstant.CHAT_ROOM;

/**
 * 操作ChatRoom的Redis工具类
 * @author mo
 **/
@Service
public class ChatRoomCacheUtilImpl implements ChatRoomCacheUtil {

    @Autowired
    private MPCache<String, String> stringCache;
    @Autowired
    private MPCache<String, ChatRoom> chatRoomCache;

    @Override
    public void createChatRoomIdentification(String createId, String name, String chatRoomId) {
        stringCache.put(CHAT_ROOM + createId+":"+name, chatRoomId);
        stringCache.put(CHAT_ROOM + chatRoomId, createId+":"+name);
        chatRoomCache.put(CHAT_ROOM+chatRoomId, new ChatRoom(chatRoomId, createId, name));
    }

    @Override
    public void addChatRoom(ChatRoom chatRoom) {
        stringCache.put(CHAT_ROOM + chatRoom.getId(), JSON.toJSONString(chatRoom));
    }

    /**
     * 删除chatRoom缓存
     * @param chatRoomId 聊天室ID
     */
    @Override
    public boolean deleteChatRoomById(String chatRoomId) {
        // 删除聊天室信息
        stringCache.invalidate(CHAT_ROOM + chatRoomId);
        // 删除聊天室点赞
        stringCache.invalidate(CachePrefixConstant.CHAT_ROOM_THUMBS_UP + chatRoomId);
        ChatRoom chatRoom = chatRoomCache.get(CHAT_ROOM+chatRoomId, k->null);
        if (chatRoom != null) {
            // 删除聊天室标识符
            stringCache.invalidate(CHAT_ROOM+chatRoom.getCreateUserId()+":"+chatRoom.getName());
            // 删除聊天室信息
            chatRoomCache.invalidate(CHAT_ROOM+chatRoomId);
        }
        return true;
    }

    /**
     * 是否存在(聊天室)
     * @param chatRoomId 聊天室ID
     */
    @Override
    public boolean existence(String chatRoomId) {
        return stringCache.get(CHAT_ROOM + chatRoomId, k->null) != null;
    }

    @Override
    public String existence(String createId, String name) {
        return stringCache.get(CHAT_ROOM+createId+":"+name, k->null);
    }

    @Override
    public ChatRoom getChatRoomById(String chatRoomId) {
        return chatRoomCache.get(CHAT_ROOM+chatRoomId, k->null);
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
