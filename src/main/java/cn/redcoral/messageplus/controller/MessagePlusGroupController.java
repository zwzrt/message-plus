package cn.redcoral.messageplus.controller;

import cn.redcoral.messageplus.data.entity.Group;
import cn.redcoral.messageplus.manage.GroupManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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



    @PutMapping
    public boolean updateGroupNameById(@RequestParam("id") String groupId, @RequestParam("name") String groupName) {
        return groupManage.updateGroupName(groupId, groupName);
    }

}
