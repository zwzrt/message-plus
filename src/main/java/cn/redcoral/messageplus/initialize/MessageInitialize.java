package cn.redcoral.messageplus.initialize;

import cn.redcoral.messageplus.utils.ChatRoomManage;
import org.springframework.context.annotation.Import;

/**
 * message-plus的初始化类
 * @author mo
 **/
@Import({ChatRoomManage.class})
public class MessageInitialize {
}
