package cn.redcoral.messageplus.data.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.redcoral.messageplus.data.entity.ChatRoom;
import cn.redcoral.messageplus.data.entity.po.ChatRoomHistoryPo;
import cn.redcoral.messageplus.data.entity.po.ChatRoomPo;
import cn.redcoral.messageplus.data.mapper.MessagePlusChatRoomCloseMapper;
import cn.redcoral.messageplus.data.mapper.MessagePlusChatRoomMapper;
import cn.redcoral.messageplus.data.service.ChatRoomService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private MessagePlusChatRoomCloseMapper chatRoomCloseMapper;

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

    @Transactional
    @Override
    public boolean closeChatRoom(String stopUserId, String chatRoomId) {
        LambdaQueryWrapper<ChatRoomPo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ChatRoomPo::getId, chatRoomId);
        lqw.eq(ChatRoomPo::getCreateUserId, stopUserId);
        ChatRoomPo chatRoomPo = chatRoomMapper.selectOne(lqw);
        // 该聊天室不存在 或者 关闭者不是创建者 或 已经关闭
        if (chatRoomPo == null) return false;
        ChatRoomHistoryPo chatRoomHistoryPo = new ChatRoomHistoryPo();
        BeanUtil.copyProperties(chatRoomPo, chatRoomHistoryPo);
        // 添加关闭时间
        chatRoomHistoryPo.setOffTime(new Timestamp(System.currentTimeMillis()));
        // 添加关闭的聊天室到表中
        boolean bo = chatRoomCloseMapper.insert(chatRoomHistoryPo)==1;
        if (!bo) return false;
        // 删除之前的表
        bo = chatRoomMapper.deleteById(chatRoomPo.getId()) == 1;
        return bo;
    }

    @Override
    public List<ChatRoom> selectChatRooms() {
        LambdaQueryWrapper<ChatRoomPo> lqw = new LambdaQueryWrapper<>();
        // 保证开始时间已经开始
        lqw.le(ChatRoomPo::getOpeningTime, new Timestamp(System.currentTimeMillis()));
        // 查询
        return ChatRoom.BuildChatRoomList(chatRoomMapper.selectList(lqw));
    }

    @Override
    public List<ChatRoom> selectChatRoomList(int page, int size) {
        // 设置查询条件
        Page<ChatRoomPo> p = new Page<>(page, size, false);
        LambdaQueryWrapper<ChatRoomPo> lqw = new LambdaQueryWrapper<>();
        // 保证开始时间已经开始
        lqw.le(ChatRoomPo::getOpeningTime, new Timestamp(System.currentTimeMillis()));
        // 查询
        IPage<ChatRoomPo> ipage = chatRoomMapper.selectPage(p, lqw);
        // 获取列表
        List<ChatRoomPo> chatRoomPoList = ipage.getRecords();
        // 封装为ChatRoom
        List<ChatRoom> chatRoomList = ChatRoom.BuildChatRoomList(chatRoomPoList);
        return chatRoomList;
    }
}
