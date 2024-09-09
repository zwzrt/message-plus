package cn.redcoral.messageplus.utils.cache;

import cn.redcoral.messageplus.data.entity.po.HistoryMessagePo;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * 单体聊天缓存
 * @author mosang
 * time 2024/8/6
 */
public interface ChatSingleCacheUtil {
    
    //获得该Id的聊天内容
    List<HistoryMessagePo> getChatSingleContent(String receiverId);
    
    void removeCache(String receiverId);
}
