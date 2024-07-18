package cn.redcoral.messageplus.service;

import cn.redcoral.messageplus.entity.Message;
import cn.redcoral.messageplus.entity.MessageType;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import cn.redcoral.messageplus.utils.MessagePlusUtils;
import cn.redcoral.messageplus.utils.CachePrefixConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;

import static cn.redcoral.messageplus.utils.BeanUtil.*;

/**
 * 消息处理器
 * @author mo
 **/
@Slf4j
@ServerEndpoint("/messageplus/ws/{sid}")
public abstract class MessagePlusService {
    /**
     * 客户端唯一标识
     */
    protected String client_id;


    /**
     * 连接建立成功调用的方法
     * @param sid 唯一ID
     */
    @OnOpen
    public void baseOnOpen(Session session, @PathParam(MessagePlusProperties.pathParamName) String sid) {
        this.client_id = sid;
        // 加入聊天
        MessagePlusUtils.joinChat(sid, this, session);
        // 调用下游方法
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
        // 删除用户的会话
        MessagePlusUtils.quitChat(this.client_id);
        // 确保开启了持久化
        if (MessagePlusProperties.persistence) {
            // 删除用户所在的服务ID
            stringRedisTemplate().delete(CachePrefixConstant.USER_SERVICE_PREFIX+client_id);
        }
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
        Message message;
        try {
            message = JSON.parseObject(messageJSON, Message.class);
        } catch (JSONException e) {
            log.error("消息数据格式错误！");
            return;
        }
        // 消息类型为空
        if (message.getType()==null||message.getType().isEmpty()) {
            log.error("消息类型为空！");
            return;
        }

        // 调用下游方法
        // 调用接收消息方法
        boolean bo1 = this.onMessage(message.getData(), session);
        boolean bo2 = false;
        // 根据消息类型调用对应方法
        switch (MessageType.valueOf(message.getType())) {
            // 单发
            case SINGLE_SHOT: {
                bo2 = this.onMessageBySingle(message.getData(), session);
                // 确保开启持久化以及消息持久化，并且消息发送失败
                if (MessagePlusProperties.persistence && MessagePlusProperties.messagePersistence && !bo1 && !bo2) {
                    // 存储消息到对方会话的数组中
                    stringRedisTemplate().opsForList().leftPush(CachePrefixConstant.USER_MESSAGES_PREFIX+message.get_id(), messageJSON);
                    // 提示系统该用户有新消息
                    publishService().publish(message.get_id());
                }
                break;
            }
            // 群发
            case MASS_SHOT: {
                List<String> offLineClientIdList = this.onMessageByMass(message.getData(), session);
                // 确保开启持久化以及消息持久化，并且消息发送失败
                if (MessagePlusProperties.persistence && MessagePlusProperties.messagePersistence && !bo1 && !offLineClientIdList.isEmpty()) {// 群发
                    // 存储到群组中每个成员的消息队列
                    for (String cid : offLineClientIdList) {
                        // 存储消息到对方会话的数组中
                        stringRedisTemplate().opsForList().leftPush(CachePrefixConstant.USER_MESSAGES_PREFIX+cid, messageJSON);
                        // 提示系统该用户有新消息
                        publishService().publish(cid);
                    }
                }
                break;
            }
            // 系统
            case SYSTEM_SHOT: {
                bo2 = this.onMessageBySystem(message.getData(), session);
                // 确保开启持久化以及消息持久化，并且消息发送失败
                if (MessagePlusProperties.persistence && MessagePlusProperties.messagePersistence && !bo1 && !bo2) {
                    // 存储消息到对方会话的数组中
                    stringRedisTemplate().opsForList().leftPush(CachePrefixConstant.USER_MESSAGES_PREFIX+message.get_id(), messageJSON);
                }
                break;
            }
        }
    }
    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     */
    public boolean onMessage(Object message, Session session) {
        return false;
    }
    /**
     * 收到单发类型的消息后调用的方法
     */
    public abstract boolean onMessageBySingle(Object message, Session session);
    /**
     * 收到群发类型的消息后调用的方法
     * @return 失败的用户ID
     */
    public abstract List<String> onMessageByMass(Object message, Session session);
    /**
     * 收到系统类型的消息后调用的方法
     */
    public abstract boolean onMessageBySystem(Object message, Session session);
    /**
     * 收到收件箱的单发消息
     */
    public abstract void onMessageByInboxAndSingle(Object message, Session session);
    /**
     * 收到收件箱的群发消息
     */
    public abstract void onMessageByInboxAndByMass(Object message, Session session);
    /**
     * 收到收件箱的系统消息
     */
    public abstract void onMessageByInboxAndSystem(Object message, Session session);

    /**
     * 处理过程中发生错误
     */
    @OnError
    public void baseOnError(Session session, Throwable error) {
        this.onError(session, error);
    }
    /**
     * 处理过程中发生错误
     */
    public abstract void onError(Session session, Throwable error);
}
