package cn.redcoral.messageplus.utils.cache;

import cn.redcoral.messageplus.data.entity.message.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 单体聊天缓存
 * @author mosang
 * time 2024/8/6
 */
public interface ChatSingleCacheUtil {
    
    //添加聊天内容
    void addChatSingleContent(String senderId, String receiverId, Message message);
    
    //移除聊天内容
    BlockingQueue removeChatSingleContent(String receiverId);
}
