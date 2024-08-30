package cn.redcoral.messageplus.controller;

import cn.hutool.http.server.HttpServerRequest;
import cn.redcoral.messageplus.data.entity.Group;
import cn.redcoral.messageplus.data.entity.message.Message;
import cn.redcoral.messageplus.handler.MessageHandler;
import cn.redcoral.messageplus.manage.GroupManage;
import cn.redcoral.messageplus.port.MessagePlusBase;
import cn.redcoral.messageplus.properties.MessagePersistenceProperties;
import cn.redcoral.messageplus.utils.CounterIdentifierUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.List;
import org.springframework.web.bind.annotation.*;

/**
 * 群聊
 * @author mo
 **/
@RestController
@RequestMapping("/messageplus/group")
public class MessagePlusGroupController {

    @Autowired
    private GroupManage groupManage;
    @Autowired
    private MessagePlusBase messagePlusBase;
    @Autowired
    private MessageHandler messageHandler;

    /**
     * 拉入黑名单
     */
    public void a() {

    }
    
    
    /**
     * 创建群组
     * @param createUserId 创建者ID
     * @param name 群组名称
     * @param client_ids 成员ID
     */
    @PostMapping
    public void createGroup(String createUserId, String name, List<String> client_ids){
        groupManage.createGroup(createUserId,name,client_ids);
    }

    /**
     * 发送群发类消息
     * @param groupId 群组ID
     * @param msg 消息体
     */
    @PostMapping("/send")
    public void sendMassMessage(HttpServerRequest request, @RequestParam("id1") String senderId,
                                @RequestParam("id2") String groupId, @RequestBody String msg) throws Exception {
        // 1.并发限流
        // 短时间发送消息达到上限，禁止发送消息
        if (CounterIdentifierUtil.isLessThanOrEqual(senderId, MessagePersistenceProperties.concurrentNumber)) {
            return;
        }
        // 计数器加一
        CounterIdentifierUtil.numberOfSendsIncrease(senderId);

        // TODO 2.查询是否禁言

        // 3.权限校验
        Message message = Message.buildMass(senderId, groupId, msg);
        // 进行权限校验
        boolean bo = messagePlusBase.onMessageCheck(request, message);
        // 权限校验不通过
        if (!bo) return;

        // 4.发送群发消息
        messageHandler.handleMassMessage(senderId, groupId, message);

        // 计数器减一
        CounterIdentifierUtil.numberOfSendsDecrease(senderId);
    }



    
    @DeleteMapping("/{groupId}")
    public boolean deleteGroup(@PathVariable("groupId") String groupId){
       return groupManage.deleteGroup(groupId);
    }




    /**
     * 模糊搜索群组
     */
    @GetMapping("/like")
    public List<Group> likeByName(@RequestParam("name") String name) {
        return groupManage.likeByName(name);
    }

    /**
     * 查询指定用户的群组
     * @return 群组列表
     */
    @GetMapping("mygroup")
    public List<Group> selectMyGroup(@RequestParam("id") String userId) {
        return groupManage.selectGroupListByCreateId(userId);
    }

    /**
     * 查询群组总人数
     * @param groupId 群组ID
     * @return 群组人数
     */
    @GetMapping("/usernum")
    public int selectUserNumById(@RequestParam("id") String groupId) {
        return groupManage.getUserNum(groupId);
    }



    @PutMapping("updatename")
    public boolean updateGroupNameById(@RequestParam("id") String groupId, @RequestParam("name") String groupName) {
        return groupManage.updateGroupName(groupId, groupName);
    }

    @PutMapping("join")
    public boolean joinGroup(@RequestParam("id") String groupId, @RequestParam("userId") String userId) {
        return groupManage.joinGroup(groupId,userId);
    }

}
