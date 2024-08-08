package cn.redcoral.messageplus.controller;

import cn.hutool.http.server.HttpServerRequest;
import cn.redcoral.messageplus.data.entity.ChatRoom;
import cn.redcoral.messageplus.manage.ChatRoomManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 聊天室
 * @author mo
 **/
@RestController
@RequestMapping("/messageplus/chatroom")
public class MessagePlusChatRoomController {

    @Autowired
    private ChatRoomManage chatRoomManage;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    /**
     * 创建聊天室
     * @param createId 创建者ID
     * @param chatRoomName 聊天室名称
     * @return 聊天室信息
     */
    @PostMapping("/create")
    public ChatRoom createChatRoom(@RequestParam("createId") String createId, @RequestParam("chatRoomName") String chatRoomName) {
        return chatRoomManage.createChatRoom(createId, chatRoomName);
    }

    /**
     * 加入聊天室
     * @param senderId 发送者ID
     * @param chatRoomId 聊天室ID
     */
    @PostMapping("/add")
    public void addChatRoom(HttpServerRequest request, @RequestParam("id1") String senderId, @RequestParam("id2") String chatRoomId) {
        chatRoomManage.joinChatRoomById(senderId, chatRoomId);
    }

    /**
     * 点赞
     * @param senderId 点赞者ID
     * @param chatRoomId 聊天室ID
     */
    @PostMapping("/thumbsUpNum")
    public void thumbsUpNum(HttpServerRequest request, @RequestParam("id1") String senderId, @RequestParam("id2") String chatRoomId) {
        chatRoomManage.thumbsUpNum(senderId, chatRoomId);
    }


}
