package cn.redcoral.messageplus.utils.cache;

/**
 * 操作ChatRoom的Redis工具类
 * @author mo
 **/
public interface ChatRoomCacheUtil {
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
     * 点赞
     * @param chatRoomId 聊天室ID
     */
    boolean thumbUpChatRoomById(String chatRoomId);

}
