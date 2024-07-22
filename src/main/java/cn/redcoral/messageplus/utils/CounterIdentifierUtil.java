package cn.redcoral.messageplus.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 计数器标识工具类
 * @author mo
 **/
public class CounterIdentifierUtil {
    private static Map<String, Integer> numberOfSendsIdentifierMap = new ConcurrentHashMap<>();


    /**
     * 计数器减一
     * @param id 标识
     */
    public static void numberOfSendsDecrease(String id) {
        numberOfSendsDecrease(id, 1);
    }

    /**
     * 计数器减少
     * @param id 标识
     * @param num 减少指定数目
     */
    public static void numberOfSendsDecrease(String id, int num) {
        Integer n = numberOfSendsIdentifierMap.getOrDefault(id, 0);
        numberOfSendsIdentifierMap.put(id, n.intValue() - num <= 0 ? 0 : n.intValue() - num);
    }

    /**
     * 计数器加一
     * @param id 标识
     */
    public static void numberOfSendsIncrease(String id) {
        numberOfSendsIncrease(id, 1);
    }

    /**
     * 计数器增加
     * @param id 标识
     * @param num 增加指定数目
     */
    public static void numberOfSendsIncrease(String id, int num) {
        Integer n = numberOfSendsIdentifierMap.getOrDefault(id, 0);
        numberOfSendsIdentifierMap.put(id, n + num);
    }

    /**
     * 是否小于于等于
     */
    public static boolean isLessThanOrEqual(String id, int num) {
        return num<=numberOfSendsIdentifierMap.getOrDefault(id, 0);
    }



}
