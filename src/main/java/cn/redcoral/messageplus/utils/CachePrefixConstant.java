package cn.redcoral.messageplus.utils;

/**
 * 缓存前缀常量
 * @author mo
 * @日期: 2024-06-16 16:24
 **/
public class CachePrefixConstant {
    /**
     * 头前缀
     */
    public static final String PREFIX_HEAD = "MESSAGE_PLUS:";
    /**
     * 用户服务绑定前缀
     */
    public static final String USER_SERVICE_PREFIX = PREFIX_HEAD + "USER_SERVICE:";
    /**
     * 用户消息数组的前缀
     */
    public static final String USER_MESSAGES_PREFIX = PREFIX_HEAD + "USER_MESSAGES:";
    /**
     * 总在线人数的键
     */
    public static final String ON_LINE_PEOPLE_NUM = PREFIX_HEAD + "ON_LINE_PEOPLE_NUM";
}
