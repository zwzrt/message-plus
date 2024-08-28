package cn.redcoral.messageplus.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author mo
 **/
@Data
@Component("messagePersistenceProperties")
@ConfigurationProperties(prefix = "messageplus.message")
@Slf4j
public class MessagePersistenceProperties {
    /**
     * 消息持久化
     */
    public static boolean messagePersistence = true;
    /**
     * 消息过期时间（默认-1永不过期）
     */
    public static int expirationTime = -1;
    /**
     * 消息并发数
     */
    public static int concurrentNumber = 1;
    /**
     * 限制消息周期的时间（单位：s）
     */
    public static int cycleRestrictionsTime = 1;
    
    /**
     * 消息超时时间（单位：分钟）
     */
    public static int messageTimeOut = 3;
    /**
     * 限制消息周期内的次数
     */
    public static int cycleRestrictionsNum = 10;
    
    /**
     * 发送失败后重试次数
     */
    public static int retryCount = 1;
    
    /**
     * 每次重试所需间隔时间(单位:ms)
     */
    public static long intervalTime = 1000;
    
    
    
    public boolean isMessagePersistence() {
        return MessagePersistenceProperties.messagePersistence;
    }
    public void setMessagePersistence(boolean messagePersistence) {
        MessagePersistenceProperties.messagePersistence = messagePersistence;
    }
    public int getExpirationTime() {
        return expirationTime;
    }
    public void setExpirationTime(int expirationTime) {
        MessagePersistenceProperties.expirationTime = expirationTime;
    }
    public int getConcurrentNumber() {
        return concurrentNumber;
    }
    public void setConcurrentNumber(int concurrentNumber) {
        MessagePersistenceProperties.concurrentNumber = concurrentNumber;
    }
    public int getCycleRestrictionsTime() {
        return cycleRestrictionsTime;
    }
    public void setCycleRestrictionsTime(int cycleRestrictionsTime) {
        MessagePersistenceProperties.cycleRestrictionsTime = cycleRestrictionsTime;
    }
    
    public int getMessageTimeOut() {
        return messageTimeOut;
    }
    
    public void setMessageTimeOut(int messageTimeOut) {
        MessagePersistenceProperties.messageTimeOut = messageTimeOut;
    }
    
    public int getCycleRestrictionsNum() {
        return cycleRestrictionsNum;
    }
    public void setCycleRestrictionsNum(int cycleRestrictionsNum) {
        MessagePersistenceProperties.cycleRestrictionsNum = cycleRestrictionsNum;
    }
    
    public int getRetryCount() {
        return retryCount;
    }
    
    public void setRetryCount(int retryCount) {
        if(retryCount<1){
            retryCount=1;
        }
        MessagePersistenceProperties.retryCount = retryCount;
    }
    
    public long getIntervalTime() {
        return intervalTime;
    }
    
    public void setIntervalTime(long intervalTime) {
        MessagePersistenceProperties.intervalTime = intervalTime;
    }
}
