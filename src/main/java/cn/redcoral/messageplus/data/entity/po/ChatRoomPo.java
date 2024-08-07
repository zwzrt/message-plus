package cn.redcoral.messageplus.data.entity.po;

import cn.hutool.core.bean.BeanUtil;
import cn.redcoral.messageplus.data.entity.ChatRoom;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static cn.redcoral.messageplus.config.MessagePlusConfig.snowflake;

/**
 * 聊天室
 * @author mo
 **/
@Data
@TableName("mp_chat_room")
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
     * 最大人数
     */
    private int maxUserNum = 0;
    /**
     * 用户ID列表
     */
    @TableField(exist = false)
    private List<String> clientIdList = new ArrayList<>();
    /**
     * 点赞数量
     */
    private long thumbsUpNum = 0;
    /**
     * 开播时间
     */
    private Timestamp openingTime = new Timestamp(System.currentTimeMillis());
    /**
     * 停播时间
     */
    private Timestamp offTime;
    /**
     * 是否关闭
     */
    private boolean isClose = false;

    {
        this.id = snowflake.nextIdStr();
    }


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
        BeanUtil.copyProperties(chatRoomPo, chatRoomPo);
        return chatRoomPo;
    }

    /**
     * 加入聊天室
     * @param userId 用户ID
     * @return 总人数
     */
    public int joinChatRoom(String userId) {
        if (userId==null||userId.isEmpty()) return -1;

        int index = clientIdList.indexOf(userId);

        // 未加入聊天室
        if (index==-1) {
            // 加入聊天室
            clientIdList.add(userId);
        }

        int size = clientIdList.size();
        if (size>maxUserNum) maxUserNum = size;
        return size;
    }
    /**
     * 退出聊天室
     * @param userId 用户ID
     * @return 总人数
     */
    public int exitChatRoom(String userId) {
        if (userId==null||userId.isEmpty()) return -1;
        // 退出聊天室
        clientIdList.remove(userId);

        return clientIdList.size();
    }

}
