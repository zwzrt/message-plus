package cn.redcoral.messageplus;

import cn.redcoral.messageplus.entity.Message;
import cn.redcoral.messageplus.entity.MessageType;
import cn.redcoral.messageplus.utils.ChatUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.tags.MessageTag;

import javax.websocket.*;
import javax.websocket.server.PathParam;

/**
 * @author mo
 * @日期: 2024-05-25 11:33
 **/
@Slf4j
public abstract class MessagePlusBase {
    /**
     * 客户端唯一标识
     */
    protected String client_id;

    /**
     * 连接建立成功调用的方法
     * @param sid 唯一ID
     */
    @OnOpen
    public void baseOnOpen(Session session, @PathParam("sid") String sid) {
        this.client_id = sid;
        // 加入聊天
        ChatUtils.joinChat(sid, session);
        this.onOpen(session, sid);
    }
    /**
     * 连接建立成功调用的方法
     */
    abstract public void onOpen(Session session, String sid);

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void baseOnClose() {
        ChatUtils.quitChat(this.client_id);
        this.onClose();
    }
    /**
     * 连接关闭调用的方法
     */
    abstract public void onClose();

    /**
     * 收到客户端消息后调用的方法
     * @param messageJSON 客户端发送过来的消息
     */
    @OnMessage
    public void baseOnMessage(String messageJSON, Session session) {
        // 当数据为空，直接结束
        if (messageJSON==null || messageJSON.isEmpty()) {
            log.warn("对方返回空数据。");
            return;
        }
        Message message = JSON.parseObject(messageJSON, Message.class);
        // 调用接收消息方法
        this.onMessage(message.getData(), session);
        //根据消息类型调用对应方法
        switch (MessageType.valueOf(message.getType())) {
            // 单发
            case SINGLE_SHOT: {
                this.onMessageBySingle(message.getData(), session);
                break;
            }
            // 群发
            case MASS_SHOT: {
                this.onMessageByMass(message.getData(), session);
                break;
            }
            // 系统
            case SYSTEM_SHOT: {
                this.onMessageBySystem(message.getData(), session);
                break;
            }
        }

    }
    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     */
    public void onMessage(Object message, Session session){}
    /**
     * 收到单发类型的消息后调用的方法
     */
    public abstract void onMessageBySingle(Object message, Session session);
    /**
     * 收到群发类型的消息后调用的方法
     */
    public abstract void onMessageByMass(Object message, Session session);
    /**
     * 收到系统类型的消息后调用的方法
     */
    public abstract void onMessageBySystem(Object message, Session session);

    /**
     * 处理过程中发生错误
     * @param session
     * @param error
     */
    @OnError
    public void baseOnError(Session session, Throwable error) {
        this.onError(session, error);
    }
    /**
     * 处理过程中发生错误
     * @param session
     * @param error
     */
    public abstract void onError(Session session, Throwable error);
}
