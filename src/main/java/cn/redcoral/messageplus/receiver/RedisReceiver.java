package cn.redcoral.messageplus.receiver;

import cn.redcoral.messageplus.utils.MessagePlusUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author mo
 **/
@Slf4j
@Component
public class RedisReceiver implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 提示系统该用户有新消息
        MessagePlusUtils.hasNewMessage(message.toString());
    }

}
