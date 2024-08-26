package cn.redcoral.messageplus.utils.cache;

import cn.redcoral.messageplus.data.entity.ChatRoom;

/**
 * 操作ChatRoom的Redis工具类
 * @author mo
 **/
public interface ChatRoomCacheUtil {

    /**
     * 创建聊天室标识符（用于查询是否存在）
     * @param createId 创建者ID
     * @param name 聊天室名称
     * @param chatRoomId 聊天室ID
     */
    void createChatRoomIdentification(String createId, String name, String chatRoomId);

    /**
     * 添加聊天室信息到缓存中
     * @param chatRoom 聊天室对象
     */
    void addChatRoom(ChatRoom chatRoom);


    /**
     * 删除chatRoom缓存
     * @param chatRoomId 聊天室ID
     */
    boolean deleteChatRoomById(String chatRoomId);

    /**
     * 是否存在
     * @param chatRoomId 聊天室ID
     */
    boolean existence(String chatRoomId);

    /**
     * 是否存在
     * @param createId 创建者ID
     * @param name 聊天室名称
     */
    String existence(String createId, String name);

    /**
     * 查询对应ID的聊天室
     * @param chatRoomId 聊天室ID
     */
    ChatRoom getChatRoomById(String chatRoomId);

    /**
     * 点赞
     * @param chatRoomId 聊天室ID
     */
    boolean thumbUpChatRoomById(String chatRoomId);

}
