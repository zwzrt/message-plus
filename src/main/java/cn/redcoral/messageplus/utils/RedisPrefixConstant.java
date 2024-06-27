package cn.redcoral.messageplus.utils;

/**
 * Redis前缀常量
 * @author mo
 * @日期: 2024-06-16 16:24
 **/
public class RedisPrefixConstant {
    /**
     * 头前缀
     */
    protected static final String PREFIX_HEAD = "MESSAGE_PLUS:";
    /**
     * 用户服务绑定前缀
     */
    public static final String USER_SERVICE_PREFIX = PREFIX_HEAD + "USER_SERVICE:";
    /**
     * 用户消息数组的前缀
     */
    public static final String USER_MESSAGES_PREFIX = PREFIX_HEAD + "USER_MESSAGES:";
}
