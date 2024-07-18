package cn.redcoral.messageplus.port;

import javax.websocket.*;
import java.util.List;

/**
 * 消息接收实现类
 * @author mo
 **/
public interface MessagePlusBase {
    /**
     * 连接建立成功调用的方法
     */
    public String onOpen(Session session, String sid);
    /**
     * 连接关闭调用的方法
     */
    public void onClose();
    /**
     * 收到单发类型的消息后调用的方法
     * @param userId 对方用户ID
     */
    public boolean onMessageBySingle(String userId, Object message);
    /**
     * 收到群发类型的消息后调用的方法
     * @return 失败的用户ID
     */
    public List<String> onMessageByMass(String groupId, Object message);
    /**
     * 收到系统类型的消息后调用的方法
     */
    public boolean onMessageBySystem(Object message);
    /**
     * 收到收件箱的单发消息
     */
    public void onMessageByInboxAndSingle(Object message, Session session);
    /**
     * 处理过程中发生错误
     */
    public void onError(Session session, Throwable error);
    /**
     * 收到收件箱的群发消息
     */
    public void onMessageByInboxAndByMass(Object message, Session session);
    /**
     * 收到收件箱的系统消息
     */
    public void onMessageByInboxAndSystem(Object message, Session session);

}
