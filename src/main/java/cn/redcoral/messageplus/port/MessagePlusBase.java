package cn.redcoral.messageplus.port;

import cn.hutool.http.server.HttpServerRequest;
import cn.redcoral.messageplus.entity.MessageType;
import org.springframework.http.HttpRequest;

import javax.websocket.*;
import java.util.List;

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
     * 收到消息时的权限校验
     * @param request HTTP请求信息
     * @param senderId 发生者ID
     * @return 是否允许发送消息
     */
    public boolean onMessageCheck(HttpServerRequest request, String senderId, MessageType mt);
    /**
     * 收到系统类型的消息后调用的方法
     * @param senderId 发送者ID
     */
    public void onMessageBySystem(String senderId, Object message);
    /**
     * 收到收件箱的单发消息
     * @param senderId 发送者ID
     * @param receiverId 接收者ID
     * @return 是否成功
     */
    public boolean onMessageByInboxAndSingle(String senderId, String receiverId, Object message);
    /**
     * 收到收件箱的群发消息
     * @param senderId 发送者ID
     * @param groupId 群组ID
     * @param receiverId 接收者ID
     * @return 是否成功
     */
    public boolean onMessageByInboxAndByMass(String senderId, String groupId, String receiverId, Object message);
    /**
     * 处理过程中发生错误
     */
    public void onError(Session session, Throwable error);

}
