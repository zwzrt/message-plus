package cn.redcoral.messageplus.utils.cache;

import cn.redcoral.messageplus.data.entity.message.Message;
import lombok.Data;

import java.util.List;

/**
 * @author mosang
 * time 2024/8/7
 */
@Data
public class MessageData {
    private String sendId;
    private List<Message> messages;
}
