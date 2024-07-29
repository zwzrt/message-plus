package cn.redcoral.messageplus.utils;

import cn.redcoral.messageplus.entity.ChatRoom;
import cn.redcoral.messageplus.entity.Group;
import cn.redcoral.messageplus.exteriorUtils.SpringUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 聊天室管理
 * @author mo
 **/
@Configuration
public class ChatRoomManage {
    public static final String CHAT_ROOM_KEY = "MESSAGEPLUS:CHATROOM:";

    /**
     * 每个用户ID对应的聊天室ID（用户ID，聊天室ID数组）
     */
    private ConcurrentHashMap<String, List<String>> userByChatRoomIdMap = new ConcurrentHashMap<>();
    /**
     * 聊天室数组（聊天室ID，聊天室）
     */
    private ConcurrentHashMap<String, ChatRoom> chatRoomByIdMap = new ConcurrentHashMap<>();

    private StringRedisTemplate stringRedisTemplate;



    /**
     * 创建聊天室
     * @param createUserId 创建者ID
     * @param name 聊天室名称
     * @return 聊天室ID
     */
    public ChatRoom createChatRoom(String createUserId, String name) {
        ChatRoom chatRoom = new ChatRoom();

        chatRoom.setCreateUserId(createUserId);
        chatRoom.setName(name);

        chatRoomByIdMap.put(chatRoom.getId(), chatRoom);

        return chatRoom;
    }

    /**
     * 创建聊天室
     * @param createUserId 创建者ID
     * @param name 聊天室名称
     * @return 聊天室ID
     */
    public ChatRoom createChatRoom(String chatRoomId, String createUserId, String name) {
        ChatRoom chatRoom = new ChatRoom();

        chatRoom.setId(chatRoomId);
        chatRoom.setCreateUserId(createUserId);
        chatRoom.setName(name);

        chatRoomByIdMap.put(chatRoom.getId(), chatRoom);

        return chatRoom;
    }

    /**
     * 记录每个用户的聊天室ID
     * @param client_id 用户ID
     * @param chatRoomId 聊天室ID
     */
    protected void addUserByChatRoomIdMap(String client_id, String chatRoomId) {
        List<String> chatRoomIdList = userByChatRoomIdMap.get(client_id);
        if (chatRoomIdList==null) {
            chatRoomIdList = new ArrayList<>();
            chatRoomIdList.add(chatRoomId);
            userByChatRoomIdMap.put(client_id, chatRoomIdList);
        } else {
            chatRoomIdList.add(chatRoomId);
        }
    }

    /**
     * 加入聊天室
     * @param client_id 用户ID
     * @param chatRoomId 聊天室ID
     */
    public void joinChatRoomById(String client_id, String chatRoomId) {
        ChatRoom chatRoom = chatRoomByIdMap.get(chatRoomId);
        if (chatRoom==null) return;
        chatRoom.joinChatRoom(client_id);
        List<String> chatRoomIdList = userByChatRoomIdMap.get(client_id);
        if (chatRoomIdList==null) {
            chatRoomIdList = new ArrayList<>();
            chatRoomIdList.add(chatRoomId);
            userByChatRoomIdMap.put(client_id, chatRoomIdList);
        } else {
            chatRoomIdList.remove(chatRoomId);
            chatRoomIdList.add(chatRoomId);
        }
    }



    /**
     * 聊天室查询接口
     * @param chatRoomId 聊天室ID
     * @return 聊天室
     */
    public ChatRoom getChatRoomById(String chatRoomId) {
        ChatRoom chatRoom = chatRoomByIdMap.get(chatRoomId);
        // 集合中不存在，向Redis查询
        if (chatRoom == null) {
            chatRoom = getChatRoomByIdInCache(chatRoomId);
        }
        return chatRoom;
    }

    /**
     * 聊天室查询接口（向缓存查询）
     * @param chatRoomId 聊天室ID
     * @return 聊天室
     */
    protected ChatRoom getChatRoomByIdInCache(String chatRoomId) {
        // 尝试注入stringRedisTemplate
        if (stringRedisTemplate == null) {
            try {
                stringRedisTemplate = SpringUtils.getBean(StringRedisTemplate.class);
            } catch (BeansException be) {}
        }
        // 若stringRedisTemplate为空，则返回null
        if (stringRedisTemplate==null) return null;
        // 缓存查询群组
        String chatRoomJson = stringRedisTemplate.opsForValue().get(CHAT_ROOM_KEY +chatRoomId);
        // 反序列化为对象
        ChatRoom chatRoom = JSON.parseObject(chatRoomJson, ChatRoom.class);
        // 加入到本地数据
        chatRoomByIdMap.put(chatRoom.getId(), chatRoom);
        // 返回
        return chatRoom;
    }
    /**
     * 获取全部聊天室
     */
    public ConcurrentHashMap<String, ChatRoom> getChatRoomList() {
        return chatRoomByIdMap;
    }

}
