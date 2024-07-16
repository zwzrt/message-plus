package cn.redcoral.messageplus.controller;

import cn.redcoral.messageplus.utils.MessagePlusUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mo
 * @日期: 2024-07-16 14:48
 **/
@RestController
@RequestMapping("/messageplus")
public class MessagePlusController {

    /**
     * 获取当前在线人数
     * @return 在线人数
     */
    @GetMapping
    public String getOnLinePeopleNum() {
        return MessagePlusUtils.getOnLinePeopleNum().toString();
    }

}
