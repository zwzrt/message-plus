package cn.redcoral.messageplus.controller;

import cn.redcoral.messageplus.data.entity.Group;
import cn.redcoral.messageplus.manage.GroupManage;
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
