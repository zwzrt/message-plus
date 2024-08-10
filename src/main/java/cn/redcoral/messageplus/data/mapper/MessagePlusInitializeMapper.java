package cn.redcoral.messageplus.data.mapper;

/**
 * 初始化表
 * @author mo
 **/
public interface MessagePlusInitializeMapper {

    /**
     * 创建消息表
     */
    void createMessage();

    /**
     * 创建群组表
     */
    void createGroup();

    /**
     * 创建群组黑名单表
     */
    void createGroupBlacklistTable();

    /**
     * 创建聊天室表
     */
    void createChatRoomTable();

    /**
     * 创建历史聊天室表
     */
    void createChatRoomHistoryTable();



}
