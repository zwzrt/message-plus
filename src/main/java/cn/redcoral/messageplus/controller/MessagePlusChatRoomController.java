package cn.redcoral.messageplus.controller;

import cn.hutool.http.server.HttpServerRequest;
import cn.redcoral.messageplus.utils.ChatRoomManage;
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
