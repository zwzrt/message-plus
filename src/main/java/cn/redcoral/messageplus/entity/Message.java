package cn.redcoral.messageplus.entity;

import lombok.Data;

/**
 * 消息类
 * @author mo
 * @日期: 2024-05-09 17:26
 **/
@Data
public class Message {
    private Integer code; // 消息编码
    private Object data;
    private String msg;
}
