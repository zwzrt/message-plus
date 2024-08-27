package cn.redcoral.messageplus.controller;

import cn.redcoral.messageplus.manage.GroupManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
