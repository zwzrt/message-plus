package cn.redcoral.messageplus.properties;

import cn.redcoral.messageplus.data.dictionary.PropertiesDictionary;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author mo
 **/
@Data
@Component("messagePersistenceProperties")
@ConfigurationProperties(prefix = "messageplus.message")
public class MessagePlusMessageProperties {
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
     * 限制消息周期内的次数
     */
    public static int cycleRestrictionsNum = 10;

    /**
     * 消息超时时间（单位：分钟）
     */
    public static int messageTimeOut = 3;

    
    /**
     * 发送失败后重试次数
     */
    public static int retryCount = 2;
    
    /**
     * 每次重试所需间隔时间(单位:ms)
     */
    public static long intervalTime = 1000;
    
    /**
     * 消息存储条数(一个群组或双人会话)
     */
    public static long storedRecords = 100;
    
    /**
     * 消息最大存储时间(单位:天)
     */
    public static int survivalTime = 5;
    
    /**
     * 检查时间间隔(单位:天)
     */
    public static int CheckTime = 1;



    static {
        // 加入字典
        PropertiesDictionary.putMessage("concurrentNumber", "消息并发数");
        PropertiesDictionary.putMessage("retryCount", "发送失败后重试次数");
        PropertiesDictionary.putMessage("intervalTime", "每次重试间隔时间");
    }


    
    public long getStoredRecords() {
        return storedRecords;
    }
    
    public void setStoredRecords(long storedRecords) {
        MessagePlusMessageProperties.storedRecords = storedRecords;
    }
    
    public int getSurvivalTime() {
        return survivalTime;
    }
    
    public void setSurvivalTime(int survivalTime) {
        MessagePlusMessageProperties.survivalTime = survivalTime;
    }
    
    public int getCheckTime() {
        return CheckTime;
    }
    
    public static void setCheckTime(int checkTime) {
        CheckTime = checkTime;
    }
    
    public boolean isMessagePersistence() {
        return MessagePlusMessageProperties.messagePersistence;
    }
    public void setMessagePersistence(boolean messagePersistence) {
        MessagePlusMessageProperties.messagePersistence = messagePersistence;
    }
    public int getExpirationTime() {
        return expirationTime;
    }
    public void setExpirationTime(int expirationTime) {
        MessagePlusMessageProperties.expirationTime = expirationTime;
    }
    public int getConcurrentNumber() {
        return concurrentNumber;
    }
    public void setConcurrentNumber(int concurrentNumber) {
        MessagePlusMessageProperties.concurrentNumber = concurrentNumber;
    }
    public int getCycleRestrictionsTime() {
        return cycleRestrictionsTime;
    }
    public void setCycleRestrictionsTime(int cycleRestrictionsTime) {
        MessagePlusMessageProperties.cycleRestrictionsTime = cycleRestrictionsTime;
    }
    
    public int getMessageTimeOut() {
        return messageTimeOut;
    }
    
    public void setMessageTimeOut(int messageTimeOut) {
        MessagePlusMessageProperties.messageTimeOut = messageTimeOut;
    }
    
    public int getCycleRestrictionsNum() {
        return cycleRestrictionsNum;
    }
    public void setCycleRestrictionsNum(int cycleRestrictionsNum) {
        MessagePlusMessageProperties.cycleRestrictionsNum = cycleRestrictionsNum;
    }
    
    public int getRetryCount() {
        return retryCount;
    }
    
    public void setRetryCount(int retryCount) {
        if(retryCount<1){
            retryCount=1;
        }
        MessagePlusMessageProperties.retryCount = retryCount;
    }
    
    public long getIntervalTime() {
        return intervalTime;
    }
    
    public void setIntervalTime(long intervalTime) {
        MessagePlusMessageProperties.intervalTime = intervalTime;
    }
}
