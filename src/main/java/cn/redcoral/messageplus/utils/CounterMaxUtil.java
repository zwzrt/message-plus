package cn.redcoral.messageplus.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 计算最大人数（同一人只算一次）
 * @author mo
 **/
public class CounterMaxUtil {

    static class M {
        private static Map<String, Map<String, Integer>> map = new HashMap<>();

        // 判断是否存在
        private static boolean isExist(String key, String id) {
            Map<String, Integer> idMap = map.get(key);
            // 不存在，说明该键第一次使用
            if (idMap == null) {
                idMap = new HashMap<>();
                idMap.put(id, 1);
                map.put(key, idMap);
                return false;
            }
            // 该ID为空，说明第一次加入
            if (idMap.get(id)==null) {
                idMap.put(id, 1);
                return false;
            }
            // 存在
            else return true;
        }
    }

    /**
     * 最大数加一
     * @param key 键
     * @param id 唯一ID（例如用户ID）
     */
    public static void plusOne(String key, String id) {
        // 不存在，说明第一次加入，则最大数加一
        if (!M.isExist(key, id)) {
            CounterIdentifierUtil.numberOfSendsIncrease(key);
        }
    }
    public static int getMaxNum(String key) {
        return CounterIdentifierUtil.getNum(key);
    }

}
