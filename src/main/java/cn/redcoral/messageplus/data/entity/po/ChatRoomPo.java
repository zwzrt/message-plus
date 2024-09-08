package cn.redcoral.messageplus.data.entity.po;

import cn.hutool.core.bean.BeanUtil;
import cn.redcoral.messageplus.data.entity.ChatRoom;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 聊天室
 * @author mo
 **/
@Data
@TableName(value = "mp_chat_room", keepGlobalPrefix = false)
public class ChatRoomPo implements Serializable {
    /**
     * 聊天室ID
     */
    private String id;
    /**
     * 创建者ID
     */
    private String createUserId;
    /**
     * 聊天室名称
     */
    private String name;
    /**
     * 开播时间
     */
    private Timestamp openingTime = new Timestamp(System.currentTimeMillis());
    /**
     * 是否开启禁言
     */
    private Boolean isForbiddenSpeak;


    /**
     * 创建聊天室
     * @param id 聊天室ID
     * @param createUserId 创建者ID
     * @param chatRoomName 聊天室名称
     * @return 聊天室
     */
    public static ChatRoomPo BuildChatRoom(String id, String createUserId, String chatRoomName) {
        ChatRoomPo chatRoomPo = new ChatRoomPo();
        chatRoomPo.id = id;
        chatRoomPo.createUserId = createUserId;
        chatRoomPo.name = chatRoomName;
        return chatRoomPo;
    }

    /**
     * 创建聊天室
     * @return 聊天室
     */
    public static ChatRoomPo BuildChatRoom(ChatRoom chatRoom) {
        ChatRoomPo chatRoomPo = new ChatRoomPo();
        BeanUtil.copyProperties(chatRoom, chatRoomPo);
        return chatRoomPo;
    }

}
