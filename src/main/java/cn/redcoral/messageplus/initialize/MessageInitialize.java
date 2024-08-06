package cn.redcoral.messageplus.initialize;

import cn.redcoral.messageplus.data.mapper.MessagePlusInitializeMapper;
import cn.redcoral.messageplus.manage.ChatRoomManage;
import cn.redcoral.messageplus.utils.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;

/**
 * message-plus的初始化类
 * @author mo
 **/
@Slf4j
@Import({ChatRoomManage.class})
public class MessageInitialize {

    static {
        log.info("消息增强器初始化中...");

        // 1、初始化数据库表结构
        // 1.1、获取mapper类
        MessagePlusInitializeMapper initializeMapper = BeanUtil.messagePlusInitializeMapper();
        // 1.2、执行语句
        initializeMapper.createMessage();
        initializeMapper.createChatroomBlacklistTable();

        log.info("消息增强器初始化完成！");
    }

}
