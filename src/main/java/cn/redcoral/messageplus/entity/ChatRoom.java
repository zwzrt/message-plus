package cn.redcoral.messageplus.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 聊天室
 * @author mo
 **/
@Data
public class ChatRoom {
    /**
     * 聊天室ID
     */
    private String id = UUID.randomUUID().toString();
    /**
     * 创建者ID
     */
    private String createUserId;
    /**
     * 聊天室名称
     */
    private String name;
    /**
     * 最大人数
     */
    private int maxUserNum = 0;
    /**
     * 用户ID列表
     */
    private List<String> clientIdList = new ArrayList<>();
    /**
     * 点赞数量
     */
    private long thumbsUpNum;
    /**
     * 开播时间
     */
    private Timestamp openingTime;
    /**
     * 停播时间
     */
    private Timestamp offTime;


    /**
     * 创建聊天室
     * @param id 聊天室ID
     * @param createUserId 创建者ID
     * @param chatRoomName 聊天室名称
     * @return 聊天室
     */
    public static ChatRoom BuildChatRoom(String id, String createUserId, String chatRoomName) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.id = id;
        chatRoom.createUserId = createUserId;
        chatRoom.name = chatRoomName;
        return chatRoom;
    }

    /**
     * 加入聊天室
     * @param userId 用户ID
     * @return 总人数
     */
    public int joinChatRoom(String userId) {
        if (userId==null||userId.isEmpty()) return -1;

        int index = clientIdList.indexOf(userId);

        // 未加入聊天室
        if (index==-1) {
            // 加入聊天室
            clientIdList.add(userId);
        }

        int size = clientIdList.size();
        if (size>maxUserNum) maxUserNum = size;
        return size;
    }
    /**
     * 退出聊天室
     * @param userId 用户ID
     * @return 总人数
     */
    public int exitChatRoom(String userId) {
        if (userId==null||userId.isEmpty()) return -1;
        // 退出聊天室
        clientIdList.remove(userId);

        return clientIdList.size();
    }

}
