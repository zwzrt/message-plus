package cn.redcoral.messageplus.entity.message;

import java.util.UUID;

/**
 * 聊天室信息
 * @author mo
 **/
public class ChatRoomMessage {
    /**
     * 聊天室ID
     */
    private String id = UUID.randomUUID().toString();
    /**
     * 点赞数量
     */
    private long thumbsUpNum;
    /**
     * 消息内容
     */
    private String content;
}
