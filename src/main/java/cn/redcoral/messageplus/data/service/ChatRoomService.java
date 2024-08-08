package cn.redcoral.messageplus.data.service;

import cn.redcoral.messageplus.data.entity.ChatRoom;

import java.util.List;

/**
 * @author mo
 **/
public interface ChatRoomService {
    ChatRoom insertChatRoom(ChatRoom chatRoom);

    boolean closeChatRoom(String stopUserId, String chatRoomId);

    List<ChatRoom> selectChatRooms();
}
