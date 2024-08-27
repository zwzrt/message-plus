package cn.redcoral.messageplus.constant;

/**
 * 缓存前缀常量
 * @author mo
 **/
public class CachePrefixConstant {
    /**
     * 头前缀
     */
    public static final String HEAD_PREFIX = "MESSAGE_PLUS:";
    /**
     * 用户服务绑定前缀
     */
    public static final String USER_SERVICE_PREFIX = HEAD_PREFIX + "USER_SERVICE:";
    /**
     * 用户消息数组的前缀
     */
    public static final String USER_MESSAGES_PREFIX = HEAD_PREFIX + "USER_MESSAGES:";
    /**
     * 总在线人数的键
     */
    public static final String ON_LINE_PEOPLE_NUM = HEAD_PREFIX + "ON_LINE_PEOPLE_NUM";

    /**
     * 聊天室
     */
    public static final String CHAT_ROOM = HEAD_PREFIX + "CHAT_ROOM:";
    /**
     * 聊天室点赞
     */
    public static final String CHAT_ROOM_THUMBS_UP = HEAD_PREFIX + "CHAT_ROOM:THUMBS_UP:";
    
    /**
     * 单聊消息存储
     */
    public static final String CHAT_SINGLE_CONTENT = HEAD_PREFIX + "CHAT_SINGLE_CONTENT:";
    
    /**
     * 群发消息存储
     */
    public static final String CHAT_GROUP_CONTENT = HEAD_PREFIX + "CHAT_GROUP_CONTENT:";
    
}
