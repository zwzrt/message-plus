package cn.redcoral.messageplus.exception.Impl;

import cn.redcoral.messageplus.exception.BaseException;

/**
 * @author mosang
 * time 2024/8/12
 */
public class MessageSendException extends RuntimeException implements BaseException {
    
    public MessageSendException() {
    }
    
    public MessageSendException(String message) {
        super(message);
    }
}
