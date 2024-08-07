package cn.redcoral.messageplus.utils.cache.impl;

import cn.redcoral.messageplus.constant.CachePrefixConstant;
import cn.redcoral.messageplus.data.entity.ChatRoom;
import cn.redcoral.messageplus.utils.CounterIdentifierUtil;
import cn.redcoral.messageplus.utils.cache.ChatRoomCacheUtil;
import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static cn.redcoral.messageplus.manage.ChatRoomManage.CHAT_ROOM_KEY;

/**
 * 操作ChatRoom的Redis工具类
 * @author mo
 **/
@Service
public class ChatRoomCacheUtilImpl implements ChatRoomCacheUtil {

    @Autowired
    private Cache<String, String> stringCache;

    @Override
    public void createChatRoomIdentification(String createId, String name, String chatRoomId) {
        stringCache.put(CHAT_ROOM_KEY + createId+":"+name, chatRoomId);
    }

    @Override
    public void addChatRoom(ChatRoom chatRoom) {
        stringCache.put(CHAT_ROOM_KEY + chatRoom.getId(), JSON.toJSONString(chatRoom));
    }

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
     * 是否存在(聊天室)
     * @param chatRoomId 聊天室ID
     */
    @Override
    public boolean existence(String chatRoomId) {
        return stringCache.get(CachePrefixConstant.CHAT_ROOM + chatRoomId, (k)->null) != null;
    }

    @Override
    public String existence(String createId, String name) {
        return stringCache.get(CHAT_ROOM_KEY+createId+":"+name, (k)->null);
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
