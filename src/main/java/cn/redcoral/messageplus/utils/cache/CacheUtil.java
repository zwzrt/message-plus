package cn.redcoral.messageplus.utils.cache;

import cn.redcoral.messageplus.data.entity.po.HistoryMessagePo;

import java.util.List;

public interface CacheUtil {
    public void addChatContent(String receiverId, HistoryMessagePo message);
    
    public void removeCache(String receiverId);
    
    public List<HistoryMessagePo> getChatSingleContent(String receiverId);
}
