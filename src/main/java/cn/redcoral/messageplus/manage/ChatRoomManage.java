package cn.redcoral.messageplus.manage;

import cn.redcoral.messageplus.data.entity.ChatRoom;
import cn.redcoral.messageplus.data.service.ChatRoomService;
import cn.redcoral.messageplus.utils.CounterIdentifierUtil;
import cn.redcoral.messageplus.utils.CounterMaxUtil;
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

    /**
     * 每个用户ID对应的聊天室ID（用户ID，聊天室ID数组）
     */
    private ConcurrentHashMap<String, List<String>> userByChatRoomIdMap = new ConcurrentHashMap<>();
    /**
     * 聊天室数组（聊天室ID，聊天室）
     */
    private ConcurrentHashMap<String, ChatRoom> chatRoomByIdMap = new ConcurrentHashMap<>();

    @Autowired
    private ChatRoomCacheUtil chatRoomCacheUtil;
    @Autowired
    private ChatRoomService chatRoomService;



    /**
     * 创建聊天室
     * @param createUserId 创建者ID
     * @param name 聊天室名称
     * @return 聊天室ID
     */
    public ChatRoom createChatRoom(String createUserId, String name) {
        // 查询该聊天室是否存在
        String value = chatRoomService.existence(createUserId, name);
        ChatRoom chatRoom = null;
        // 该聊天室不存在，进行创建
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

            // 添加聊天室缓存（用于判断是否存在）
            chatRoomCacheUtil.createChatRoomIdentification(createUserId, name, chatRoom.getId());
            // 添加聊天室信息缓存
            chatRoomCacheUtil.addChatRoom(chatRoom);
            // 返回聊天室信息
            return chatRoom;
        }
        chatRoom = new ChatRoom();
        chatRoom.setId(value);
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
     * 根据聊天室ID加入指定的聊天室，并更新用户与聊天室的映射关系
     * 如果聊天室不存在，则不进行任何操作
     * 如果用户不在任何聊天室中，创建一个新的列表来存储该用户加入的聊天室ID
     * 如果用户已经在聊天室列表中，先移除再添加，以更新聊天室状态
     *
     * @param client_id 用户ID，用于标识用户
     * @param chatRoomId 聊天室ID，用于查找和加入特定的聊天室
     */
    public void joinChatRoomById(String client_id, String chatRoomId) {
        // 通过聊天室ID获取聊天室对象
        ChatRoom chatRoom = chatRoomByIdMap.get(chatRoomId);
        // 如果聊天室不存在，直接返回不进行任何操作
        if (chatRoom == null) return;
        // 用户加入聊天室
        chatRoom.joinChatRoom(client_id);
        // 获取用户加入的聊天室列表
        List<String> chatRoomIdList = userByChatRoomIdMap.get(client_id);
        // 如果用户之前没有加入任何聊天室，创建一个新的列表并添加当前聊天室ID
        if (chatRoomIdList == null) {
            chatRoomIdList = new ArrayList<>();
            chatRoomIdList.add(chatRoomId);
            userByChatRoomIdMap.put(client_id, chatRoomIdList);
        } else {
            // 如果用户已经在列表中，先移除再添加，以更新聊天室状态
            chatRoomIdList.remove(chatRoomId);
            chatRoomIdList.add(chatRoomId);
        }
        // 增加聊天室最大人数
        CounterMaxUtil.plusOne("chatroom:maxUserNum:" + chatRoomId, client_id);
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

    public List<ChatRoom> selectChatRoomList(int page, int size) {
        return chatRoomService.selectChatRoomList(page, size);
    }

    /**
     * 给聊天室点赞点赞
     * @param senderId 点赞者ID
     * @param chatRoomId 聊天室ID
     */
    public void thumbsUpNum(String senderId, String chatRoomId) {
        chatRoomService.upvote(senderId, chatRoomId);
    }

}
