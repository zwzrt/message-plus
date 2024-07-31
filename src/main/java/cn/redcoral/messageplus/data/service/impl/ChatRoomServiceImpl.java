package cn.redcoral.messageplus.data.service.impl;

import cn.redcoral.messageplus.data.entity.vo.ChatRoom;
import cn.redcoral.messageplus.data.mapper.ChatRoomMapper;
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
    private ChatRoomMapper chatRoomMapper;

    @Override
    public ChatRoom insertChatRoom(ChatRoom chatRoom) {
        if (chatRoom == null) return null;
        // 查询是否已经存在
        LambdaQueryWrapper<ChatRoom> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ChatRoom::getCreateUserId, chatRoom.getCreateUserId())
                .eq(ChatRoom::getName, chatRoom.getName());
        ChatRoom chatRoom1;
        try {
            chatRoom1 = chatRoomMapper.selectOne(lqw);
        } catch (TooManyResultsException e) {
            log.error(e.getMessage());
            return null;
        }
        if (chatRoom1 != null) return chatRoom1;
        // 插入
        if(chatRoomMapper.insert(chatRoom)==1) {
            return chatRoom;
        } else {
            return null;
        }
    }

    @Override
    public boolean closeChatRoom(String stopUserId, String chatRoomId) {
        LambdaQueryWrapper<ChatRoom> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ChatRoom::getId, chatRoomId);
        lqw.eq(ChatRoom::getCreateUserId, stopUserId);
        lqw.eq(ChatRoom::isClose, false);
        ChatRoom chatRoom = chatRoomMapper.selectOne(lqw);
        // 该聊天室不存在 或者 关闭者不是创建者 或 已经关闭
        if (chatRoom == null) return false;
        // 添加关闭时间
        chatRoom.setOffTime(new Timestamp(System.currentTimeMillis()));
        // 设置关闭
        chatRoom.setClose(true);
        return chatRoomMapper.updateById(chatRoom)==1;
    }

    @Override
    public List<ChatRoom> selectChatRooms() {
        LambdaQueryWrapper<ChatRoom> lqw = new LambdaQueryWrapper<>();
        // 保证开始时间已经开始
        lqw.le(ChatRoom::getOpeningTime, new Timestamp(System.currentTimeMillis()));
        // 保证关闭时间还没有到
        lqw.ge(ChatRoom::getOffTime, new Timestamp(System.currentTimeMillis()))
                .or()
                .eq(ChatRoom::getOffTime, null);
        // 查询
        return chatRoomMapper.selectList(lqw);
    }
}
