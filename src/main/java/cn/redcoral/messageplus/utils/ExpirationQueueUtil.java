package cn.redcoral.messageplus.utils;

import cn.redcoral.messageplus.data.entity.message.Message;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 消息过期队列工具类
 * @author mo
 **/
public class ExpirationQueueUtil {

    /**
     * 存活时间
     */
    private long survivalTime;
    /**
     * 最大长度
     */
    private int messageMaxSize = 20;
    /**
     * 存储多个过期队列
     */
    private final Map<String, Queue> map = new ConcurrentHashMap<>();


    class Queue {
        /**
         * 集合内的最老时间
         */
        private long oldestTime = 0L;
        // 设置排序规则
        private TreeSet<Message> survivalSet = new TreeSet<>(new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                long t1 = o1.getCreateTime().getTime();
                long t2 = o2.getCreateTime().getTime();
                if (t1==t2) return 1;
                else return (int) (t1 - t2);
            }
        });

        public void add(Message message) {
            this.survivalSet.add(message);
        }

        public int size() {
            return this.survivalSet.size();
        }
    }

    /**
     * @param survivalTime 存活时间
     * @param survivalTimeUnit 存活时间单位
     */
    public ExpirationQueueUtil(long survivalTime, TimeUnit survivalTimeUnit, int messageMaxSize) {
        // 全部转化为毫秒单位的时间
        this.survivalTime = survivalTimeUnit.toMillis(survivalTime);
        this.messageMaxSize = messageMaxSize;
    }

    public Set<Message> getSurvivalSet(String key) {
        Queue queue = map.get(key);
        if (queue == null) return null;
        // 最老时间 小于 当前时间 减去 存活时间，说明set集合中存在过期数据
        // 触发删除过期机制
        // 限制最老时间
        long limitTheMaximumAge = System.currentTimeMillis() - this.survivalTime;
        if (queue.oldestTime < limitTheMaximumAge) {
            // 迭代器遍历
            Iterator<Message> it = queue.survivalSet.iterator();
            while (it.hasNext()) {
                // 比限制最老时间还老，则删除
                if (it.next().getCreateTime().getTime() <= limitTheMaximumAge) {
                    it.remove();
                }
                // 若没有，则退出循环，因为set集合是从老到新排序，无需继续遍历
                else break;
            }
        }
        // 返回
        return queue.survivalSet;
    }

    public List<Message> getSurvivalList(String key) {
        Set<Message> survivalSet = this.getSurvivalSet(key);
        if (survivalSet == null) return null;
        else return new ArrayList<>(survivalSet);
    }

    public void add(String key, Message message) {
        Queue queue = this.map.get(key);
        if (queue == null) {
            queue = new Queue();
            queue.oldestTime = message.getCreateTime().getTime();
            this.map.put(key, queue);
        } else {
            // 保存最老时间
            if (queue.oldestTime > message.getCreateTime().getTime()) {
                queue.oldestTime = message.getCreateTime().getTime();
            }
            // 保证不超过最大长度
            if (queue.size() >= messageMaxSize) {
                // 删除最老的消息
                queue.survivalSet.pollFirst();
            }
        }
        // 加入有序Set集合
        queue.add(message);
    }

}
