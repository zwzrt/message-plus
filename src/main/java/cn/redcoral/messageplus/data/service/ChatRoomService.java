package cn.redcoral.messageplus.data.service;

import cn.redcoral.messageplus.data.entity.ChatRoom;
import cn.redcoral.messageplus.data.entity.po.ChatRoomPo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;

/**
 * @author mo
 **/
public interface ChatRoomService {
    ChatRoom insertChatRoom(ChatRoom chatRoom);

    boolean closeChatRoom(String stopUserId, ChatRoom chatRoom);

    List<ChatRoom> selectChatRooms();

    /**
     * 查询聊天室
     */
    ChatRoom selectChatRoomById(String id);

    /**
     * 查询聊天室分页
     * @param page 当前页数
     * @param size 每页大小
     */
    List<ChatRoom> selectChatRoomList(int page, int size);

    /**
     * 判断是否存在
     * @param chatRoomId 聊天室ID
     */
    boolean existence(String chatRoomId);

    /**
     * 判断是否存在
     * @param createUserId 创建者ID
     * @param name 聊天室ID
     */
    String existence(String createUserId, String name);

    /**
     * 点赞
     * @param senderId 点赞者ID
     * @param chatRoomId 聊天室ID
     */
    void upvote(String senderId, String chatRoomId);
}
