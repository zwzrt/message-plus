package cn.redcoral.messageplus.data.dictionary;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置文件字典
 * @author mo
 **/
public class PropertiesDictionary {
    private static final Map<String, String> map = new HashMap<>();


    /**
     * 获取字典名称
     */
    public static String getValue(String key) {
        return map.get(key);
    }
    public static String getMessageValue(String key) {
        return getValue("message."+key);
    }
    public static String getChatroomValue(String key) {
        return getValue("chatroom."+key);
    }

    /**
     * 添加键值对
     */
    public static void put(String key, String value) {
        map.put(key, value);
    }
    public static void putMessage(String key, String value) {
        put("message."+key, value);
    }
    public static void putChatroom(String key, String value) {
        put("chatroom."+key, value);
    }
}
