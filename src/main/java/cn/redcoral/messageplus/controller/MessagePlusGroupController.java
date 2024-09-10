package cn.redcoral.messageplus.controller;

import cn.redcoral.messageplus.data.entity.Group;
import cn.redcoral.messageplus.data.entity.message.Message;
import cn.redcoral.messageplus.handler.MessageHandler;
import cn.redcoral.messageplus.manage.GroupManage;
import cn.redcoral.messageplus.port.MessagePlusBase;
import cn.redcoral.messageplus.port.MessagePlusUtil;
import cn.redcoral.messageplus.properties.MessagePlusMessageProperties;
import cn.redcoral.messageplus.utils.CounterIdentifierUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 群聊
 *
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
     *
     * @param createUserId 创建者ID
     * @param name         群组名称
     * @param client_ids   成员ID
     */
    @PostMapping
    public Integer createGroup(@RequestHeader(value = "token", required = false) String token,
                               String createUserId,
                               String name,
                               List<String> client_ids) {
        boolean flag = MessagePlusUtil.checkIdAndToken(token, createUserId);
        if (!flag)
        {
            return 4001;
        }
        Group group = groupManage.createGroup(createUserId, name, client_ids);
        if (group == null)
        {
            return 4002;
        }
        return 2000;
    }
    
    /**
     * 发送群发类消息
     *
     * @param groupId 群组ID
     * @param msg     消息体
     */
    @PostMapping("/send")
    public Integer sendMassMessage( @RequestParam("id1") String sendId, @RequestHeader(value =
            "token", required = false) String token, @RequestParam("id2") String groupId, @RequestBody String msg) throws Exception {
        // 1.并发限流
        // 短时间发送消息达到上限，禁止发送消息
        if (CounterIdentifierUtil.isLessThanOrEqual(sendId, MessagePlusMessageProperties.concurrentNumber)) {
            return 4000;
        }
        // 计数器加一
        CounterIdentifierUtil.numberOfSendsIncrease(sendId);
        try {
            boolean flag = MessagePlusUtil.checkIdAndToken(token, sendId);
            if (!flag) {
                return 4000;
            }
            
            // 2.查询是否禁言
            boolean isForbidden = groupManage.seerchForbiddenSpeech(groupId);
            if (isForbidden) {
                return 4001;
            }
            
            // 3.发送群发消息
            Message message = Message.buildMass(sendId, groupId, msg);
            messageHandler.handleMassMessage(sendId, groupId, message);
            
            return 2000;
        } finally {
            // 计数器减一
            CounterIdentifierUtil.numberOfSendsDecrease(sendId);
        }
    }
    
    
    /**
     * 删除群组
     *
     * @param groupId 群组ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{groupId}")
    public boolean deleteGroup(@RequestHeader(value = "token", required = false) String token,
                               @RequestParam("userId") String userId, @PathVariable("groupId") String groupId) {
        boolean flag = MessagePlusUtil.checkIdAndToken(token, userId);
        if (!flag)
        {
            return false;
        }
        return groupManage.deleteGroup(groupId, userId);
    }
    
    
    /**
     * 查询群组总数
     */
    @GetMapping("/num")
    public String getNum() {
        return String.valueOf(groupManage.getGroupNum());
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
     *
     * @return 群组列表
     */
    @GetMapping("mygroup")
    public List<Group> selectMyGroup(@RequestParam("id") String userId) {
        return groupManage.selectGroupListByCreateId(userId);
    }
    
    /**
     * 查询群组总人数
     *
     * @param groupId 群组ID
     * @return 群组人数
     */
    @GetMapping("/usernum")
    public int selectUserNumById(@RequestParam("id") String groupId) {
        return groupManage.getUserNum(groupId);
    }
    
    
    /**
     * 修改群组名称
     *
     * @param groupId   群组ID
     * @param groupName 新群组名称
     * @return 是否修改成功
     */
    @PutMapping("updatename")
    public boolean updateGroupNameById(@RequestHeader(value = "token", required = false) String token,
                                       @RequestParam("userId") String userId,
                                       @RequestParam("id") String groupId, @RequestParam("name") String groupName) {
        boolean flag = MessagePlusUtil.checkIdAndToken(token, userId);
        if (!flag)
        {
            return false;
        }
        return groupManage.updateGroupName(userId, groupId, groupName);
    }
    
    /**
     * 加入群组
     *
     * @param groupId 群组ID
     * @param userId  用户ID
     * @return 是否加入成功
     */
    @PutMapping("join")
    public boolean joinGroup(@RequestHeader(value = "token", required = false) String token,
                             @RequestParam("id") String groupId,
                             @RequestParam("userId") String userId) {
        boolean flag = MessagePlusUtil.checkIdAndToken(token, userId);
        if (!flag)
        {
            return false;
        }
        return groupManage.joinGroup(groupId, userId);
    }
    
    /**
     * 退出群组
     *
     * @param groupId 群组Id
     * @param userId  用户Id
     * @return 退出是否成功
     */
    @PutMapping("signout")
    public boolean signOut(@RequestHeader(value = "token", required = false) String token,
                           @RequestParam("id") String groupId,
                           @RequestParam("userId") String userId) {
        boolean flag = MessagePlusUtil.checkIdAndToken(token, userId);
        if (!flag)
        {
            return false;
        }
        return groupManage.signOutGroup(groupId, userId);
    }
    
    /**
     * 群组禁言/解禁
     *
     * @param token   TOKEN
     * @param groupId 群组Id
     * @return 更新状态，true禁言，false未禁言
     */
    @PutMapping("forspeech")
    public boolean forbiddenSpeech(@RequestHeader(value = "token", required = false) String token,
                                   @RequestParam("userId") String userId,
                                   @RequestParam("id") String groupId) {
        boolean flag = MessagePlusUtil.checkIdAndToken(token, userId);
        if (!flag)
        {
            return false;
        }
        return groupManage.forbiddenSpeech(token, userId, groupId);
    }
    
    @GetMapping("searchforspeech")
    public boolean forbiddenSpeech(@RequestParam("id") String groupId) {
        return groupManage.seerchForbiddenSpeech(groupId);
    }
    
    //TODO 被踢出群组
}
