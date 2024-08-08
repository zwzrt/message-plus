package cn.redcoral.messageplus.data.service.impl;

import cn.redcoral.messageplus.data.entity.ChatRoom;
import cn.redcoral.messageplus.data.entity.po.ChatRoomPo;
import cn.redcoral.messageplus.data.mapper.MessagePlusChatRoomMapper;
import cn.redcoral.messageplus.data.service.ChatRoomService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author mo
 **/
@Slf4j
@Service
public class ChatRoomServiceImpl implements ChatRoomService {
    @Autowired
    private MessagePlusChatRoomMapper chatRoomMapper;

    @Override
    public ChatRoom insertChatRoom(ChatRoom chatRoom) {
        if (chatRoom == null) return null;
        // 查询是否已经存在
        LambdaQueryWrapper<ChatRoomPo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ChatRoomPo::getCreateUserId, chatRoom.getCreateUserId())
                .eq(ChatRoomPo::getName, chatRoom.getName());
        ChatRoomPo chatRoomPo1;
        try {
            chatRoomPo1 = chatRoomMapper.selectOne(lqw);
        } catch (TooManyResultsException e) {
            log.error(e.getMessage());
            return null;
        }
        if (chatRoomPo1 != null) return ChatRoom.BuildChatRoom(chatRoomPo1);
        // 插入
        if(chatRoomMapper.insert(ChatRoomPo.BuildChatRoom(chatRoom))==1) {
            return chatRoom;
        } else {
            return null;
        }
    }

    @Override
    public boolean closeChatRoom(String stopUserId, String chatRoomId) {
        LambdaQueryWrapper<ChatRoomPo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ChatRoomPo::getId, chatRoomId);
        lqw.eq(ChatRoomPo::getCreateUserId, stopUserId);
        lqw.eq(ChatRoomPo::isClose, false);
        ChatRoomPo chatRoomPo = chatRoomMapper.selectOne(lqw);
        // 该聊天室不存在 或者 关闭者不是创建者 或 已经关闭
        if (chatRoomPo == null) return false;
        // 添加关闭时间
        chatRoomPo.setOffTime(new Timestamp(System.currentTimeMillis()));
        // 设置关闭
        chatRoomPo.setClose(true);
        return chatRoomMapper.updateById(chatRoomPo)==1;
    }

    @Override
    public List<ChatRoom> selectChatRooms() {
        LambdaQueryWrapper<ChatRoomPo> lqw = new LambdaQueryWrapper<>();
        // 保证开始时间已经开始
        lqw.le(ChatRoomPo::getOpeningTime, new Timestamp(System.currentTimeMillis()));
        // 保证关闭时间还没有到
        lqw.ge(ChatRoomPo::getOffTime, new Timestamp(System.currentTimeMillis()))
                .or()
                .eq(ChatRoomPo::getOffTime, null);
        // 查询
        return ChatRoom.BuildChatRoomList(chatRoomMapper.selectList(lqw));
    }
}
