package cn.redcoral.messageplus.controller;

import cn.redcoral.messageplus.manage.GroupManage;
import org.springframework.beans.factory.annotation.Autowired;
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
     * 查询群组总人数
     * @param groupId 群组ID
     * @return 群组人数
     */
    @GetMapping
    public int selectUserNumById(@RequestParam("id") String groupId) {
        return groupManage.getUserNum(groupId);
    }

    @PutMapping
    public boolean updateGroupNameById(@RequestParam("id") String groupId, @RequestParam("name") String groupName) {
        return groupManage.updateGroupName(groupId, groupName);
    }

}
