package cn.redcoral.messageplus.port;

import javax.websocket.*;

/**
 * 开发者实现类
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
     * 收到系统消息
     * @param senderId 发送者ID
     * @param message 消息内容
     */
    public void onMessageBySystem(String senderId, String message);
    /**
     * 登录接口
     * @param username 用户名
     * @param password 密码
     * @return token（为null或""时登录失败）
     */
    public String login(String username, String password);
}
