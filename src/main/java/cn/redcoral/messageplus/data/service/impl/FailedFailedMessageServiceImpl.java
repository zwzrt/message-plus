package cn.redcoral.messageplus.data.service.impl;

import cn.redcoral.messageplus.data.entity.message.Message;
import cn.redcoral.messageplus.data.entity.po.FailedMessagePo;
import cn.redcoral.messageplus.data.mapper.MessagePlusFailedMessageMapper;
import cn.redcoral.messageplus.data.service.FailedMessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mo
 **/
@Service
public class FailedFailedMessageServiceImpl implements FailedMessageService {

    @Autowired
    private MessagePlusFailedMessageMapper messageMapper;


    @Transactional
    @Override
    public boolean insertMessage(Message message) {
        // 消息创建时间不能为空
        if (message.getCreateTime() == null) return false;
        // 插入并返回结果
        return messageMapper.insert(new FailedMessagePo(message)) == 1;
    }

    @Override
    public void removeMessageBySenderId(String senderId) {
        LambdaQueryWrapper<FailedMessagePo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(FailedMessagePo::getSenderId, senderId);
        messageMapper.delete(lqw);
    }

    @Override
    public List<Message> popMessageBySenderId(String senderId) {
        LambdaQueryWrapper<FailedMessagePo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(FailedMessagePo::getSenderId, senderId);
        List<FailedMessagePo> failedMessagePoList = messageMapper.selectList(lqw);
        messageMapper.delete(lqw);
        return Message.BuildMessageList(failedMessagePoList);
    }

    @Override
    public List<Message> popMessageByReceiverId(String receiverId) {
        LambdaQueryWrapper<FailedMessagePo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(FailedMessagePo::getReceiverId, receiverId);
        List<FailedMessagePo> failedMessagePoList = messageMapper.selectList(lqw);
        messageMapper.delete(lqw);
        return Message.BuildMessageList(failedMessagePoList);
    }

    @Override
    public List<Message> selectMessageBySenderId(String senderId) {
        LambdaQueryWrapper<FailedMessagePo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(FailedMessagePo::getSenderId, senderId);
        List<FailedMessagePo> failedMessagePoList = messageMapper.selectList(lqw);
        return Message.BuildMessageList(failedMessagePoList);
    }

    @Override
    public List<Message> selectMessageByReceiverId(String receiverId) {
        LambdaQueryWrapper<FailedMessagePo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(FailedMessagePo::getReceiverId, receiverId);
        List<FailedMessagePo> failedMessagePoList = messageMapper.selectList(lqw);
        return Message.BuildMessageList(failedMessagePoList);
    }
}
