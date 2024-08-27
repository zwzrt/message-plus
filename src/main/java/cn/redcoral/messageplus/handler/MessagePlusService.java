package cn.redcoral.messageplus.handler;

import cn.redcoral.messageplus.config.MessageEncoder;
import cn.redcoral.messageplus.data.entity.message.Message;
import cn.redcoral.messageplus.manage.MessageManage;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import cn.redcoral.messageplus.utils.BeanUtil;
import cn.redcoral.messageplus.utils.cache.ChatSingleCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.BlockingQueue;

/**
 * 消息处理器
 *
 * @author mo
 **/
@Slf4j
@Component
@ServerEndpoint(value = "/messageplus/ws/{sid}", encoders = {MessageEncoder.class})
public class MessagePlusService {
    /**
     * 客户端唯一标识
     */
    protected String client_id;
    
    
    /**
     * 连接建立成功调用的方法
     *
     * @param sid 唯一ID
     */
    @OnOpen
    public void baseOnOpen(Session session, @PathParam(MessagePlusProperties.pathParamName) String sid) {
        this.client_id = sid;
        // 加入聊天
        MessageManage.joinChat(sid, this, session);
        // TODO 消息重发，直接调用messagecahche遍历发送，简化代码
        ChatSingleCacheUtil chatSingleCacheUtil = BeanUtil.chatSingleCache();
        BlockingQueue blockingQueue = chatSingleCacheUtil.removeChatSingleContent(sid);
        if(blockingQueue!=null){
            //准备重发
            log.info("准备消息重发");
            new Thread(()->{
                while (true){
                    Message message = (Message) blockingQueue.poll();
                    if(message==null){
                        break;
                    }
                    MessageManage.sendMessage(sid,message);
                }
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
        MessageManage.quitChat(this.client_id);
        BeanUtil.messagePlusBase().onClose(this.client_id);
    }
    
    /**
     * 收到客户端消息后调用的方法
     *
     * @param messageJSON 客户端发送过来的消息
     */
    @OnMessage
    public void baseOnMessage(String messageJSON, Session session) {}
    
    /**
     * 处理过程中发生错误
     */
    @OnError
    public void baseOnError(Session session, Throwable error) {
        log.error("发送错误：session: {}", session);
        error.printStackTrace();
    }
}
