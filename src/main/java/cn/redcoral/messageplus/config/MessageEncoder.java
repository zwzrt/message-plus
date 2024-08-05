package cn.redcoral.messageplus.config;

import cn.redcoral.messageplus.entity.message.Message;
import com.alibaba.fastjson.JSON;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * @author mo
 **/
public class MessageEncoder implements Encoder.Text<Message> {
    @Override
    public String encode(Message object) throws EncodeException {
        return JSON.toJSONString(object);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {}

    @Override
    public void destroy() {}
}
