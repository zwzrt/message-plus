package cn.redcoral.messageplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author mo
 **/
@Controller
public class MessagePlusStaticController {
    @RequestMapping(value = {
            "/messageplus",
            "/messageplus/login",
            "/messageplus/home/**"
    })
    public String messageplus() {
        return "forward://messageplus/index.html";
    }
}
