package cn.redcoral.messageplus.initialize;

import cn.redcoral.messageplus.data.mapper.MessagePlusInitializeMapper;
import cn.redcoral.messageplus.manage.ChatRoomManage;
import cn.redcoral.messageplus.utils.BeanUtil;
import cn.redcoral.messageplus.utils.exterior.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Import;

import java.net.InetAddress;
import java.net.UnknownHostException;

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


        // 2、输出管理系统访问地址
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String LOCALHOST = localHost.toString();
            String IP = localHost.getHostAddress();
            String HOSTNAME = localHost.getHostName();
            String PORT = SpringUtils.getBean(ServerProperties.class).getPort().toString();
            log.info("管理系统访问地址：http://{}:{}/messageplus/manage/index.html", IP, PORT);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

}
