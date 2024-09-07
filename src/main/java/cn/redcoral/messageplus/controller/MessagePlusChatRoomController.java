package cn.redcoral.messageplus.controller;

import cn.hutool.http.server.HttpServerRequest;
import cn.redcoral.messageplus.data.entity.ChatRoom;
import cn.redcoral.messageplus.data.entity.message.Message;
import cn.redcoral.messageplus.data.service.ChatRoomService;
import cn.redcoral.messageplus.manage.ChatRoomManage;
import cn.redcoral.messageplus.port.MessagePlusBase;
import cn.redcoral.messageplus.properties.MessagePersistenceProperties;
import cn.redcoral.messageplus.properties.MessagePlusChatRoomProperties;
import cn.redcoral.messageplus.utils.CounterIdentifierUtil;
import cn.redcoral.messageplus.utils.ExpirationQueueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private ChatRoomService chatRoomService;
    @Autowired
    private MessagePlusBase messagePlusBase;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 消息过期队列
     */
    private static final ExpirationQueueUtil expirationQueueUtil = new ExpirationQueueUtil(MessagePlusChatRoomProperties.survivalTime, TimeUnit.SECONDS, MessagePlusChatRoomProperties.messageMaxSize);



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
     * 发送聊天室消息
     * @param senderId 发送者ID
     * @param chatRoomId 聊天室ID
     * @param msg 消息体
     */
    @PostMapping("/send")
    public void sendChatRoomMessage(HttpServerRequest request, @RequestParam("id1") String senderId, @RequestParam("id2") String chatRoomId, @RequestBody Object msg) throws Exception {
        // 1.并发限流
        // 短时间发送消息达到上限，禁止发送消息
        if (CounterIdentifierUtil.isLessThanOrEqual(senderId, MessagePersistenceProperties.concurrentNumber)) {
            return;
        }
        // 计数器加一
        CounterIdentifierUtil.numberOfSendsIncrease(senderId);

        // TODO 2.查询是否禁言


        // 3.发送消息
        Message message = Message.buildChatRoom(senderId, chatRoomId, msg);
        // 广播
        simpMessagingTemplate.convertAndSend("/messageplus/chatroom/"+chatRoomId, message);

        // 4.临时存储消息
        expirationQueueUtil.add("chatroom:"+chatRoomId, message);

        // 计数器减一
        CounterIdentifierUtil.numberOfSendsDecrease(senderId);
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
     * 获取聊天室总数
     */
    @GetMapping("/num")
    public String getNum() {
        return String.valueOf(chatRoomManage.getNum());
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
     * 查询未关闭的聊天室
     * @param userId 创建者ID
     * @return 未关闭的聊天室ID
     */
    @GetMapping("/noclose")
    public List<ChatRoom> selectMyChatRoomList(HttpServerRequest request, @RequestParam("id") String userId) {
        return chatRoomService.selectNotCloseChatRoomListByCreateId(userId);
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

    /**
     * 查询聊天室的历史消息
     * @param chatRoomId 聊天室ID
     * @return 历史消息
     */
    @GetMapping("/history/message")
    public List<Message> selectChatRoomHistoryMessage(HttpServerRequest request, @RequestParam("id") String chatRoomId) {
        return expirationQueueUtil.getSurvivalList("chatroom:"+chatRoomId);
    }


}
