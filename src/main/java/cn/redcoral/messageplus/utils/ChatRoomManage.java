package cn.redcoral.messageplus.utils;

import cn.redcoral.messageplus.data.entity.vo.ChatRoom;
import cn.redcoral.messageplus.data.service.ChatRoomService;
import cn.redcoral.messageplus.exteriorUtils.SpringUtils;
import cn.redcoral.messageplus.redis.utils.ChatRoomRedisUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
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
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, ChatRoom> chatRoomRedisTemplate;
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private ChatRoomRedisUtil chatRoomRedisUtil;



    /**
     * 创建聊天室
     * @param createUserId 创建者ID
     * @param name 聊天室名称
     * @return 聊天室ID
     */
    public ChatRoom createChatRoom(String createUserId, String name) {
        // 查询该数组是否存在
        String value = stringRedisTemplate.opsForValue().get(CHAT_ROOM_KEY+createUserId+":"+name);
        ChatRoom chatRoom = null;
        // 该群组不存在，进行创建
        if (value == null) {
            // 调用原始方法
            chatRoom = new ChatRoom();
            chatRoom.setCreateUserId(createUserId);
            chatRoom.setName(name);

            // 数据库创建
            chatRoom = chatRoomService.insertChatRoom(chatRoom);
            if (chatRoom == null) {
                return null;
            }

            chatRoomByIdMap.put(chatRoom.getId(), chatRoom);

            // 添加群组缓存（用于判断是否存在）
            stringRedisTemplate.opsForValue().set(CHAT_ROOM_KEY + createUserId+":"+name, chatRoom.getId());
            // 添加群组信息缓存
            stringRedisTemplate.opsForValue().set(CHAT_ROOM_KEY + chatRoom.getId(), JSON.toJSONString(chatRoom));
            // 返回群组信息
            return chatRoom;
        }
        chatRoom = new ChatRoom();
        chatRoom.setId(value);
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

        // 数据库创建
        chatRoom = chatRoomService.insertChatRoom(chatRoom);
        if (chatRoom == null) {
            return null;
        }

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
        if (chatRoom == null) return;
        chatRoom.joinChatRoom(client_id);
        List<String> chatRoomIdList = userByChatRoomIdMap.get(client_id);
        if (chatRoomIdList == null) {
            chatRoomIdList = new ArrayList<>();
            chatRoomIdList.add(chatRoomId);
            userByChatRoomIdMap.put(client_id, chatRoomIdList);
        } else {
            chatRoomIdList.remove(chatRoomId);
            chatRoomIdList.add(chatRoomId);
        }
    }


    /**
     * 关闭聊天室
     * @param client_id 关闭者ID
     * @param chatRoomId 聊天室ID
     */
    public boolean closeChatRoomById(String client_id, String chatRoomId) {
        // 1、操作数据库
        boolean bo = chatRoomService.closeChatRoom(client_id, chatRoomId);
        if (!bo) return false;
        // 2、删除缓存
        return chatRoomRedisUtil.deleteChatRoomById(chatRoomId);
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

    public List<ChatRoom> selectChatRooms() {
        return chatRoomService.selectChatRooms();
    }
}
