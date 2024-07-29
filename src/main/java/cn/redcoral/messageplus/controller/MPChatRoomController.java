package cn.redcoral.messageplus.controller;

import cn.hutool.http.server.HttpServerRequest;
import cn.redcoral.messageplus.utils.ChatRoomManage;
import cn.redcoral.messageplus.utils.MessagePlusUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 加入
 * @author mo
 **/
@RestController
@RequestMapping("/messageplus/chatroom")
public class MPChatRoomController {

    @Autowired
    private ChatRoomManage chatRoomManage;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    /**
     * 创建聊天室
     * @param createId 创建者ID
     * @param chatRoomName 聊天室名称
     * @return 聊天室ID
     */
    @PostMapping("create")
    public String createChatRoom(@RequestParam("createId") String createId, @RequestParam("chatRoomName") String chatRoomName) {
        return chatRoomManage.createChatRoom("05a78901-87a8-439a-b7f9-668cf13a7a39", createId, chatRoomName).getId();
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

}
