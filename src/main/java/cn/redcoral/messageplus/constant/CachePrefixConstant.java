package cn.redcoral.messageplus.constant;

/**
 * 缓存前缀常量
 * @author mo
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

    /**
     * 聊天室
     */
    public static final String CHAT_ROOM = PREFIX_HEAD + "CHAT_ROOM:";
    /**
     * 聊天室点赞
     */
    public static final String CHAT_ROOM_THUMBS_UP = PREFIX_HEAD + "CHAT_ROOM:THUMBS_UP:";
    
    public static final String CHAT_SINGLE_CONTENT = PREFIX_HEAD + "CHAT_SINGLE_CONTENT:";
}
