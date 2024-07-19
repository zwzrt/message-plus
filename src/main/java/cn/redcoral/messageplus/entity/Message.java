package cn.redcoral.messageplus.entity;

import lombok.Data;

/**
 * 消息类
 * @author mo
 **/
@Data
public class Message {
    private Integer code; // 消息编码
    private String type; // 消息类型
    private String senderId; // 发送者ID
    private String groupId; // 群组ID
    private String receiverId; // 接收者ID
    private Object data;

    public Message() {
    }
    public Message(Integer code, String type, Object data) {
        this.code = code;
        this.type = type;
        this.data = data;
    }
    public Message(Integer code, String type, String senderId, String receiverId, Object data) {
        this.code = code;
        this.type = type;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.data = data;
    }
    public static Message buildSingle(String senderId, String receiverId, Object data) {
        return new Message(200, MessageType.SINGLE_SHOT.name(), senderId, receiverId, data);
    }
    public static Message buildMass(String senderId, String groupId, String receiverId, Object data) {
        Message message = new Message(200, MessageType.MASS_SHOT.name(), senderId, receiverId, data);
        message.setGroupId(groupId);
        return message;
    }
}
