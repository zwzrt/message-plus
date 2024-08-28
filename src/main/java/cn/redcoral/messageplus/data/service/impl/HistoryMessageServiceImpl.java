package cn.redcoral.messageplus.data.service.impl;

import cn.redcoral.messageplus.data.entity.message.Message;
import cn.redcoral.messageplus.data.entity.po.HistoryMessagePo;
import cn.redcoral.messageplus.data.mapper.MessagePlusHistoryMessageMapper;
import cn.redcoral.messageplus.data.service.HistoryMessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 历史消息 Service
 * @author mo
 **/
@Service
public class HistoryMessageServiceImpl implements HistoryMessageService {

    @Autowired
    private MessagePlusHistoryMessageMapper messageMapper;


    @Transactional
    @Override
    public Long insertMessage(Message message,boolean isFail) {
        // 消息创建时间不能为空
        if (message.getCreateTime() == null) return null;
        // 插入并返回结果
        HistoryMessagePo historyMessagePo = new HistoryMessagePo(message, isFail);
        messageMapper.insert(historyMessagePo);
        return historyMessagePo.getId();
    }
    
    @Transactional
    @Override
    public boolean updateMessage(Long id, boolean isFail) {
        UpdateWrapper<HistoryMessagePo> lpw = new UpdateWrapper<>();
        lpw.eq("id",id);
        lpw.set("is_fail",isFail);
        int isOk = messageMapper.update(lpw);
        return isOk>0;
    }
    
    @Override
    public void removeMessageBySenderId(String senderId) {
        LambdaQueryWrapper<HistoryMessagePo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(HistoryMessagePo::getSenderId, senderId);
        messageMapper.delete(lqw);
    }

    @Override
    public List<Message> popMessageBySenderId(String senderId) {
        LambdaQueryWrapper<HistoryMessagePo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(HistoryMessagePo::getSenderId, senderId);
        List<HistoryMessagePo> historyMessagePoList = messageMapper.selectList(lqw);
        messageMapper.delete(lqw);
        return Message.BuildHistoryMessageList(historyMessagePoList);
    }

    @Override
    public List<Message> popMessageByReceiverId(String receiverId) {
        LambdaQueryWrapper<HistoryMessagePo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(HistoryMessagePo::getReceiverId, receiverId);
        List<HistoryMessagePo> historyMessagePoList = messageMapper.selectList(lqw);
        messageMapper.delete(lqw);
        return Message.BuildHistoryMessageList(historyMessagePoList);
    }

    @Override
    public List<Message> selectMessageBySenderId(String senderId) {
        LambdaQueryWrapper<HistoryMessagePo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(HistoryMessagePo::getSenderId, senderId);
        List<HistoryMessagePo> historyMessagePoList = messageMapper.selectList(lqw);
        return Message.BuildHistoryMessageList(historyMessagePoList);
    }

    @Override
    public List<Message> selectMessageByReceiverId(String receiverId) {
        LambdaQueryWrapper<HistoryMessagePo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(HistoryMessagePo::getReceiverId, receiverId);
        List<HistoryMessagePo> historyMessagePoList = messageMapper.selectList(lqw);
        return Message.BuildHistoryMessageList(historyMessagePoList);
    }
}
