package cn.redcoral.messageplus.data.service;

import cn.redcoral.messageplus.data.entity.message.Message;

import java.util.List;

/**
 * 消息Service层
 * @author mo
 **/
public interface HistoryMessageService {

    /**
     * 存储消息
     * @param message 消息
     * @return 是否成功
     */
    Long insertMessage(Message message,boolean isFail);

    boolean updateMessage(Long id,boolean isFail);

    /**
     * 通过发送者ID删除对应消息
     * @param senderId 发送者ID
     */
    void removeMessageBySenderId(String senderId);

    /**
     * 通过发送者ID获取对应消息并删除
     * @param senderId 发送者ID
     * @return 消息列表
     */
    List<Message> popMessageBySenderId(String senderId);

    /**
     * 通过接收者ID获取对应消息并删除
     * @param receiverId 接收者ID
     * @return 消息列表
     */
    List<Message> popMessageByReceiverId(String receiverId);



    /**
     * 通过发送者ID查询对应消息
     * @param senderId 发送者ID
     * @return 消息列表
     */
    List<Message> selectMessageBySenderId(String senderId);

    /**
     * 通过接收者ID查询对应消息
     * @param receiverId 接收者ID
     * @return 消息列表
     */
    List<Message> selectMessageByReceiverId(String receiverId);

}
