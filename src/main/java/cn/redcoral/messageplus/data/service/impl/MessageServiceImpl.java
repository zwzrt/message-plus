package cn.redcoral.messageplus.data.service.impl;

import cn.redcoral.messageplus.data.entity.message.Message;
import cn.redcoral.messageplus.data.entity.po.MessagePo;
import cn.redcoral.messageplus.data.mapper.MessagePlusMessageMapper;
import cn.redcoral.messageplus.data.service.MessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mo
 **/
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessagePlusMessageMapper messageMapper;


    @Transactional
    @Override
    public boolean insertMessage(Message message) {
        // 消息创建时间不能为空
        if (message.getCreateTime() == null) return false;
        // 插入并返回结果
        return messageMapper.insert(new MessagePo(message)) == 1;
    }

    @Override
    public void removeMessageBySenderId(String senderId) {
        LambdaQueryWrapper<MessagePo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(MessagePo::getSenderId, senderId);
        messageMapper.delete(lqw);
    }

    @Override
    public List<Message> popMessageBySenderId(String senderId) {
        LambdaQueryWrapper<MessagePo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(MessagePo::getSenderId, senderId);
        List<MessagePo> messagePoList = messageMapper.selectList(lqw);
        messageMapper.delete(lqw);
        return Message.BuildMessageList(messagePoList);
    }

    @Override
    public List<Message> popMessageByReceiverId(String receiverId) {
        LambdaQueryWrapper<MessagePo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(MessagePo::getReceiverId, receiverId);
        List<MessagePo> messagePoList = messageMapper.selectList(lqw);
        messageMapper.delete(lqw);
        return Message.BuildMessageList(messagePoList);
    }

    @Override
    public List<Message> selectMessageBySenderId(String senderId) {
        LambdaQueryWrapper<MessagePo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(MessagePo::getSenderId, senderId);
        List<MessagePo> messagePoList = messageMapper.selectList(lqw);
        return Message.BuildMessageList(messagePoList);
    }

    @Override
    public List<Message> selectMessageByReceiverId(String receiverId) {
        LambdaQueryWrapper<MessagePo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(MessagePo::getReceiverId, receiverId);
        List<MessagePo> messagePoList = messageMapper.selectList(lqw);
        return Message.BuildMessageList(messagePoList);
    }
}
