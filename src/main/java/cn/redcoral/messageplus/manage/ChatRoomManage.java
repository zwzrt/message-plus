package cn.redcoral.messageplus.manage;

import cn.redcoral.messageplus.entity.ChatRoom;
import cn.redcoral.messageplus.utils.cache.ChatRoomCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

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
    private List<String> chatRoomIdList = new ArrayList<>();
    @Autowired
    private ChatRoomCacheUtil chatRoomCacheUtil;



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
        // 删除缓存
        return chatRoomCacheUtil.deleteChatRoomById(chatRoomId);
    }




    /**
     * 聊天室查询接口
     * @param chatRoomId 聊天室ID
     * @return 聊天室
     */
    public ChatRoom getChatRoomById(String chatRoomId) {
        ChatRoom chatRoom = chatRoomByIdMap.get(chatRoomId);
        return chatRoom;
    }

    /**
     * 点赞
     * @param senderId 点赞者ID
     * @param chatRoomId 聊天室ID
     */
    public void thumbsUpNum(String senderId, String chatRoomId) {
    }

}
