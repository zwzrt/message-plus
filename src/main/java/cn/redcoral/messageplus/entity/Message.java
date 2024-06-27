package cn.redcoral.messageplus.entity;

import lombok.Data;

/**
 * 消息类
 * @author mo
 * @日期: 2024-05-09 17:26
 **/
@Data
public class Message {
    private Integer code; // 消息编码
    private String type; // 消息类型
    private String _id; // 用户ID或群组ID，为系统消息时，可以为空
    private Object data;

    public Message() {
    }
    public Message(Integer code, String type, Object data) {
        this.code = code;
        this.type = type;
        this.data = data;
    }
}
