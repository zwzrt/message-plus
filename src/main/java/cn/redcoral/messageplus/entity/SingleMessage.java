package cn.redcoral.messageplus.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 单聊消息类
 * @author mo
 **/
@Data
public class SingleMessage implements Serializable {
    private String id;
    private String senderId; // 发送者ID
    private String receiverId; // 接收者ID
    private String content; // 消息内容
    private String lifeCycle; // 生命周期
    private Timestamp createTime; // 创建时间
}
