package cn.redcoral.messageplus.port;

import cn.hutool.http.server.HttpServerRequest;
import cn.redcoral.messageplus.entity.Message;

import javax.websocket.*;

/**
 * 消息接收实现类
 * @author mo
 **/
public interface MessagePlusBase {
    /**
     * 连接建立成功调用的方法
     * @return 有返回，则使用返回值作为标识，否则使用sid作为标识
     */
    public String onOpen(Session session, String sid);
    /**
     * 连接关闭调用的方法
     */
    public void onClose(String sid);
    /**
     * 发送消息时的权限校验
     * @param request HTTP请求信息
     * @param message 消息对象
     * @return 是否允许发送消息
     */
    public boolean onMessageCheck(HttpServerRequest request, Message message) throws Exception;
    /**
     * 失败消息的方法调用
     * @param message 失败消息
     */
    public void onFailedMessage(Message message);
    /**
     * 收到系统消息
     * @param senderId 发送者ID
     * @param message 消息内容
     */
    public void onMessageBySystem(String senderId, String message);
}
