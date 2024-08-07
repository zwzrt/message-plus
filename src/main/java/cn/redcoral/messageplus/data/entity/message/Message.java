package cn.redcoral.messageplus.data.entity.message;

import cn.hutool.core.bean.BeanUtil;
import cn.redcoral.messageplus.data.entity.po.MessagePo;
import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息类
 * 用于封装不同类型的即时通讯消息
 * @author mo
 **/
@Data
public class Message {
    private Integer code; // 消息编码
    private String type; // 消息类型
    private String senderId; // 发送者ID
    private String groupId; // 群组ID
    private String chatRoomId; // 聊天室ID
    private long thumbsUpNum; // 点赞数量
    private String receiverId; // 接收者ID
    private Object data; // 消息数据
    private Timestamp createTime = new Timestamp(System.currentTimeMillis()); // 创建时间

    // 默认构造函数
    public Message() {
    }

    /**
     * 构造单发消息对象
     * @param code 消息编码
     * @param type 消息类型
     * @param data 消息数据
     */
    public Message(Integer code, String type, Object data) {
        this.code = code;
        this.type = type;
        this.data = data;
    }

    /**
     * 构造点对点消息对象
     * @param code 消息编码
     * @param type 消息类型
     * @param senderId 发送者ID
     * @param receiverId 接收者ID
     * @param data 消息数据
     */
    public Message(Integer code, String type, String senderId, String receiverId, Object data) {
        this.code = code;
        this.type = type;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.data = data;
    }

    /**
     * 创建单发消息
     * @param senderId 发送者ID
     * @param receiverId 接收者ID
     * @param data 消息数据
     * @return 创建的消息对象
     */
    public static Message buildSingle(String senderId, String receiverId, Object data) {
        return new Message(200, MessageType.SINGLE_SHOT.name(), senderId, receiverId, data);
    }

    /**
     * 创建群发消息（不指定接收者）
     * 实际上是广播消息到指定群组
     * @param senderId 发送者ID
     * @param groupId 群组ID
     * @param data 消息数据
     * @return 创建的消息对象
     */
    public static Message buildMass(String senderId, String groupId, Object data) {
        return buildMass(senderId, groupId, null, data);
    }

    /**
     * 创建群发消息（可选接收者）
     * @param senderId 发送者ID
     * @param groupId 群组ID
     * @param receiverId 接收者ID 可为null，表示广播到整个群组
     * @param data 消息数据
     * @return 创建的消息对象
     */
    public static Message buildMass(String senderId, String groupId, String receiverId, Object data) {
        Message message = new Message(200, MessageType.MASS_SHOT.name(), senderId, receiverId, data);
        message.setGroupId(groupId);
        return message;
    }

    /**
     * 创建聊天室消息
     * @param senderId 发送者ID
     * @param chatRoomId 聊天室ID
     * @param data 消息数据
     * @return 创建的消息对象
     */
    public static Message buildChatRoom(String senderId, String chatRoomId, Object data) {
        Message message = new Message();
        message.setCode(2000);
        message.setType(MessageType.CHAT_ROOM_SHOT.name());
        message.setSenderId(senderId);
        message.setChatRoomId(chatRoomId);
        message.setData(data);
        return message;
    }

    /**
     * 创建系统消息
     * @param senderId 发送者ID
     * @param data 消息数据
     * @return 创建的消息对象
     */
    public static Message buildSystem(String senderId, Object data) {
        Message message = new Message();
        message.setType(MessageType.SYSTEM_SHOT.name());
        message.setSenderId(senderId);
        message.setData(data);
        return message;
    }

    public static Message BuildMessage(MessagePo messagePo) {
        Message message = new Message();
        BeanUtil.copyProperties(messagePo, message);
        message.setData(JSON.parse(messagePo.getData()));
        return message;
    }

    public static List<Message> BuildMessageList(List<MessagePo> messagePoList) {
        List<Message> messageList = new ArrayList<>();
        for (MessagePo po : messagePoList) {
            messageList.add(BuildMessage(po));
        }
        return messageList;
    }

}
