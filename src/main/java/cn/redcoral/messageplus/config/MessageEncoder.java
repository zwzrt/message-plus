package cn.redcoral.messageplus.config;

import cn.redcoral.messageplus.entity.message.Message;
import com.alibaba.fastjson.JSON;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Message的WebSocket编码器实现类，负责将Message对象转换为JSON格式的字符串，
 * 以便通过WebSocket发送. 该编码器使用了FastJSON库来完成对象的序列化.
 *
 * @author mo
 */
public class MessageEncoder implements Encoder.Text<Message> {
    /**
     * 将Message对象编码为JSON格式的字符串.
     *
     * @param object 需要编码的Message对象.
     * @return 编码后的JSON字符串.
     * @throws EncodeException 如果编码过程中发生错误.
     */
    @Override
    public String encode(Message object) throws EncodeException {
        return JSON.toJSONString(object);
    }

    /**
     * 初始化编码器. 在此处可以进行一些初始化操作
     *
     * @param endpointConfig 终端配置信息，可用于初始化编码器的状态.
     */
    @Override
    public void init(EndpointConfig endpointConfig) {}

    /**
     * 销毁编码器. 可以在此处进行资源清理等操作
     */
    @Override
    public void destroy() {}
}
