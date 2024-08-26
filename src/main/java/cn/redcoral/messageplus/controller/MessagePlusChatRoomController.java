package cn.redcoral.messageplus.controller;

import cn.hutool.http.server.HttpServerRequest;
import cn.redcoral.messageplus.data.entity.ChatRoom;
import cn.redcoral.messageplus.manage.ChatRoomManage;
import org.springframework.beans.factory.annotation.Autowired;
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



    /**
     * 关闭聊天室
     * @param closeId 关闭者ID
     * @param chatRoomId 聊天室ID
     */
    @DeleteMapping("/close")
    public boolean closeChatRoom(HttpServerRequest request, @RequestParam("id1") String closeId, @RequestParam("id2") String chatRoomId) {
        return chatRoomManage.closeChatRoomById(closeId, chatRoomId);
    }
    /**
     * 退出聊天室
     * @param quitId 退出者ID
     * @param chatRoomId 聊天室ID
     */
    @DeleteMapping("/quit")
    public void quitChatRoom(HttpServerRequest request, @RequestParam("id1") String quitId, @RequestParam("id2") String chatRoomId) {
        chatRoomManage.quitChatRoomById(quitId, chatRoomId);
    }



    /**
     * 查询聊天室分页
     * @param page 当前页码
     * @param size 每页大小
     * @return 聊天室数组
     */
    @GetMapping
    public List<ChatRoom> selectChatRoomList(HttpServerRequest request, @RequestParam("page") int page, @RequestParam("size") int size) {
        return chatRoomManage.selectChatRoomList(page, size);
    }

    /**
     * 获取聊天室当前人数
     */
    @GetMapping("/userNum")
    public String selectChatRoomUserNumById(HttpServerRequest request, @RequestParam("id") String chatRoomId) {
        return String.valueOf(chatRoomManage.getUserNum(chatRoomId));
    }
    /**
     * 获取聊天室最大人数
     */
    @GetMapping("/maxUserNum")
    public String selectChatRoomMaxUserNumById(HttpServerRequest request, @RequestParam("id") String chatRoomId) {
        return String.valueOf(chatRoomManage.getMaxUserNum(chatRoomId));
    }
    /**
     * 获取聊天室总共人数
     */
    @GetMapping("/allUserNum")
    public String selectChatRoomAllUserNumById(HttpServerRequest request, @RequestParam("id") String chatRoomId) {
        return String.valueOf(chatRoomManage.getAllUserNum(chatRoomId));
    }


}
