package cn.redcoral.messageplus.service;

import cn.redcoral.messageplus.entity.Message;
import cn.redcoral.messageplus.entity.MessageType;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import cn.redcoral.messageplus.utils.BeanUtil;
import cn.redcoral.messageplus.utils.MessagePlusUtils;
import cn.redcoral.messageplus.constant.CachePrefixConstant;
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
public class MessagePlusService {
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
        BeanUtil.messagePlusBase().onOpen(session, sid);
    }

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
        BeanUtil.messagePlusBase().onClose(this.client_id);
    }

    /**
     * 收到客户端消息后调用的方法
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
        BeanUtil.messagePlusBase().onError(session, error);
    }

}
