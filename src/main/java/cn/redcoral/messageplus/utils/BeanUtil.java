package cn.redcoral.messageplus.utils;

import cn.redcoral.messageplus.config.cache.CacheConfig;
import cn.redcoral.messageplus.data.mapper.MessagePlusInitializeMapper;
import cn.redcoral.messageplus.data.service.HistoryMessageService;
import cn.redcoral.messageplus.data.service.UserBlacklistService;
import cn.redcoral.messageplus.handler.MessageHandler;
import cn.redcoral.messageplus.manage.ChatRoomManage;
import cn.redcoral.messageplus.port.MessagePlusBase;
import cn.redcoral.messageplus.properties.MessagePersistenceProperties;
import cn.redcoral.messageplus.properties.MessagePlusProperties;
import cn.redcoral.messageplus.utils.cache.CacheUtil;
import cn.redcoral.messageplus.utils.cache.ChatGroupCacheUtil;
import cn.redcoral.messageplus.utils.cache.ChatSingleCacheUtil;
import cn.redcoral.messageplus.utils.exterior.SpringUtils;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * 获取对象的工具类
 * 本类通过Spring上下文获取所需的Bean，以避免在每次使用时重复获取，提高代码的复用性和效率
 * 主要包含了系统配置、消息处理、缓存和消息模板等核心组件的获取方法
 * @author mo
 */
public class BeanUtil {
    // 系统配置对象，用于访问系统级别的配置信息
    private static MessagePlusProperties messagePlusProperties1;
    // 消息处理基础服务对象，用于处理消息相关的业务逻辑
    private static MessagePlusBase messagePlusBase1;
    // 消息处理器对象，用于具体的消息处理操作
    private static MessageHandler messageHandler1;
    // 聊天室管理对象，用于管理和操作聊天室的相关功能
    private static ChatRoomManage chatRoomManage1;
    // 消息模板对象，用于发送简单消息
    private static SimpMessagingTemplate simpMessagingTemplate1;
    private static MessagePlusInitializeMapper messagePlusInitializeMapper1;
    
    private static CacheUtil chatCache;
    
    private static ChatGroupCacheUtil chatGroupCache;
    
    private static MessagePersistenceProperties messagePersistenceProperties;
    
    private static HistoryMessageService historyMessageService;

    private static UserBlacklistService userBlacklistService;



    public static HistoryMessageService historyService(){
        if (historyMessageService == null) historyMessageService = SpringUtils.getBean(HistoryMessageService.class);
        return historyMessageService;
    }
    
    public static MessagePersistenceProperties messagePersistenceProperties(){
        if (messagePersistenceProperties == null) messagePersistenceProperties = SpringUtils.getBean(MessagePersistenceProperties.class);
        return messagePersistenceProperties;
    }
    
    public static ChatGroupCacheUtil chatGroupCacheUtil(){
        if (chatGroupCache == null) chatGroupCache = SpringUtils.getBean(ChatGroupCacheUtil.class);
        return chatGroupCache;
    }
    
    public static CacheUtil chatCache(){
        if (chatCache == null) chatCache = SpringUtils.getBean(CacheUtil.class);
        return chatCache;
    }
    /**
     * 获取MessagePlusProperties对象
     * 如果对象尚未初始化，则从Spring上下文中获取
     * @return MessagePlusProperties对象实例
     */
    public static MessagePlusProperties messagePlusProperties() {
        if (messagePlusProperties1 == null) messagePlusProperties1 = SpringUtils.getBean(MessagePlusProperties.class);
        return messagePlusProperties1;
    }
    
    /**
     * 获取字符串-长整型缓存对象
     * 用于缓存字符串和长整型的映射关系
     * @return Cache对象，键为字符串，值为长整型
     */
    public static Cache<String, Long> stringLongCache() {
        return CacheConfig.stringLongCache;
    }
    
    /**
     * 获取MessagePlusBase对象
     * 如果对象尚未初始化，则从Spring上下文中获取
     * @return MessagePlusBase对象实例
     */
    public static MessagePlusBase messagePlusBase() {
        if (messagePlusBase1 == null) messagePlusBase1 = SpringUtils.getBean(MessagePlusBase.class);
        return messagePlusBase1;
    }
    
    /**
     * 获取MessageHandler对象
     * 如果对象尚未初始化，则从Spring上下文中获取
     * @return MessageHandler对象实例
     */
    public static MessageHandler messageHandler() {
        if (messageHandler1 == null) messageHandler1 = SpringUtils.getBean(MessageHandler.class);
        return messageHandler1;
    }
    
    /**
     * 获取ChatRoomManage对象
     * 如果对象尚未初始化，则从Spring上下文中获取
     * @return ChatRoomManage对象实例
     */
    public static ChatRoomManage chatRoomManage() {
        if (chatRoomManage1 == null) chatRoomManage1 = SpringUtils.getBean(ChatRoomManage.class);
        return chatRoomManage1;
    }
    
    /**
     * 获取SimpMessagingTemplate对象
     * 如果对象尚未初始化，则从Spring上下文中获取
     * @return SimpMessagingTemplate对象实例
     */
    public static SimpMessagingTemplate simpMessagingTemplate() {
        if (simpMessagingTemplate1 == null) simpMessagingTemplate1 = SpringUtils.getBean(SimpMessagingTemplate.class);
        return simpMessagingTemplate1;
    }

    public static MessagePlusInitializeMapper messagePlusInitializeMapper() {
        if (messagePlusInitializeMapper1 == null) messagePlusInitializeMapper1 = SpringUtils.getBean(MessagePlusInitializeMapper.class);
        return messagePlusInitializeMapper1;
    }

    public static UserBlacklistService userBlacklistService() {
        if (userBlacklistService == null) userBlacklistService = SpringUtils.getBean(UserBlacklistService.class);
        return userBlacklistService;
    }
}
