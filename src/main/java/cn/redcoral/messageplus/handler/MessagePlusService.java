package cn.redcoral.messageplus.handler;

import cn.redcoral.messageplus.config.MessageEncoder;
import cn.redcoral.messageplus.data.entity.message.Message;
import cn.redcoral.messageplus.data.entity.po.HistoryMessagePo;
import cn.redcoral.messageplus.data.service.HistoryMessageService;
import cn.redcoral.messageplus.port.MessagePlusUtil;
import cn.redcoral.messageplus.properties.MessagePersistenceProperties;
import cn.redcoral.messageplus.manage.UserManage;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import cn.redcoral.messageplus.utils.BeanUtil;
import cn.redcoral.messageplus.utils.RetryUtil;
import cn.redcoral.messageplus.utils.cache.ChatSingleCacheUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * 消息处理器
 *
 * @author mo
 **/
@Slf4j
@Component
@ServerEndpoint(value = "/messageplus/ws/{id}/{token}", encoders = {MessageEncoder.class})
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
    public void baseOnOpen(Session session, @PathParam("id") String sid, @PathParam("token") String token) throws IOException {
        // 未登录，拒绝连接
        if (MessagePlusProperties.tokenExpirationTime!=0 && (token == null || !MessagePlusUtil.isOnlineByToken(token))) {
            session.close();
            return;
        }
        this.client_id = sid;
        // 加入聊天
        UserManage.joinChat(sid, this, session);
        // 消息重发，直接调用messagecahche遍历发送，简化代码
        ChatSingleCacheUtil chatSingleCacheUtil = BeanUtil.chatSingleCache();
        BlockingQueue blockingQueue = chatSingleCacheUtil.getChatSingleContent(sid);
        
        MessagePersistenceProperties properties = BeanUtil.messagePersistenceProperties();
        HistoryMessageService historyMessageService = BeanUtil.historyService();
        
        if (blockingQueue != null)
        {
            //准备重发
            while (true)
            {
                HistoryMessagePo message = (HistoryMessagePo) blockingQueue.poll();
                if (message == null)
                {
                    // 队列为空删除缓存
                    chatSingleCacheUtil.removeCache(sid);
                    break;
                }
                //取到了消息准备重发
                RetryUtil.retry(properties.getRetryCount(), properties.getIntervalTime(),
                        () -> {
                            Message msg = cn.hutool.core.bean.BeanUtil.copyProperties(message, Message.class);
                            msg.setData(JSON.parse(message.getData()));
                            boolean flag = UserManage.sendMessage(sid,
                                    msg);
                            if (!flag) {
                                throw new RuntimeException();
                            }
                        }
                        , (bo) -> {
                            if(bo){
                                //成功重发，修改数据库
                                historyMessageService.updateMessage(message.getId(),false);
                            }
                        });
            }
            
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
        UserManage.quitChat(this.client_id);
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
