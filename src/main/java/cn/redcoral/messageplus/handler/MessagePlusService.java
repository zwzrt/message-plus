package cn.redcoral.messageplus.handler;

import cn.redcoral.messageplus.config.MessageEncoder;
import cn.redcoral.messageplus.data.entity.message.Message;
import cn.redcoral.messageplus.manage.MessagePlusUtils;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import cn.redcoral.messageplus.utils.BeanUtil;
import cn.redcoral.messageplus.utils.cache.ChatSingleCacheUtil;
import cn.redcoral.messageplus.utils.cache.MessageData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 消息处理器
 *
 * @author mo
 **/
@Slf4j
@ServerEndpoint(value = "/messageplus/ws/{sid}", encoders = {MessageEncoder.class})
public class MessagePlusService {
    /**
     * 客户端唯一标识
     */
    protected String client_id;
    
    private static final Class<MessagePlusService> MessagePlusBase = MessagePlusService.class;
    
    
    /**
     * 连接建立成功调用的方法
     *
     * @param sid 唯一ID
     */
    @OnOpen
    public void baseOnOpen(Session session, @PathParam(MessagePlusProperties.pathParamName) String sid) {
        log.info("连接成功");
        this.client_id = sid;
        // 加入聊天
        MessagePlusUtils.joinChat(sid, this, session);
        // TODO 消息重发
        ChatSingleCacheUtil chatSingleCacheUtil = BeanUtil.chatSingleCache();
        CopyOnWriteArrayList<MessageData> messageDatas = chatSingleCacheUtil.removeChatSingleContent(sid);
        log.info("opop");
        if (messageDatas != null)
        {
            log.info("消息重发");
            new Thread(() -> {
                messageDatas.forEach((MessageData) -> {
                    List<Message> messages = MessageData.getMessages();
                    synchronized (MessagePlusBase)
                    {
                        for (Message message : messages)
                        {
                            MessagePlusUtils.sendMessage(sid, message);
                        }
                    }
                });
                
            }).start();
        }
        
        // 调用下游方法
        BeanUtil.messagePlusBase().onOpen(session, sid);
    }
    
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void baseOnClose() {
        // 删除用户的会话
        MessagePlusUtils.quitChat(this.client_id);
        BeanUtil.messagePlusBase().onClose(this.client_id);
    }
    
    /**
     * 收到客户端消息后调用的方法
     *
     * @param messageJSON 客户端发送过来的消息
     */
    @OnMessage
    public void baseOnMessage(String messageJSON, Session session) {
    }
    
    /**
     * 处理过程中发生错误
     */
    @OnError
    public void baseOnError(Session session, Throwable error) {
        log.error("发送错误：session: {}", session);
        error.printStackTrace();
    }
}
