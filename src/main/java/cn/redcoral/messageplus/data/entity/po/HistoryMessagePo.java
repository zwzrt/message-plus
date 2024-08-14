package cn.redcoral.messageplus.data.entity.po;

import cn.hutool.core.bean.BeanUtil;
import cn.redcoral.messageplus.data.entity.message.Message;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.sql.Timestamp;

/**
 * 历史消息类
 * 用于封装不同类型的即时通讯消息
 * @author mo
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@TableName("mp_history_message")
public class HistoryMessagePo {
    private Long id; // 消息ID
    private Integer code; // 消息编码
    private String type; // 消息类型
    private String senderId; // 发送者ID
    private String groupId; // 群组ID
    private String chatRoomId; // 聊天室ID
    private String receiverId; // 接收者ID
    private String data; // 消息数据
    private Timestamp createTime; // 创建时间

    /**
     * 构造单发消息对象
     * @param code 消息编码
     * @param type 消息类型
     * @param data 消息数据
     */
    public HistoryMessagePo(Integer code, String type, Object data) {
        this.code = code;
        this.type = type;
        this.data = JSON.toJSONString(data);
    }

    /**
     * 构造点对点消息对象
     * @param code 消息编码
     * @param type 消息类型
     * @param senderId 发送者ID
     * @param receiverId 接收者ID
     * @param data 消息数据
     */
    public HistoryMessagePo(Integer code, String type, String senderId, String receiverId, Object data) {
        this.code = code;
        this.type = type;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.data = JSON.toJSONString(data);
    }

    /**
     * 构造点对点消息对象
     */
    public HistoryMessagePo(Message message) {
        BeanUtil.copyProperties(message, this);
        this.data = JSON.toJSONString(message.getData());
    }


    public void setData(Object data) {
        this.data = JSON.toJSONString(data);
    }

}
