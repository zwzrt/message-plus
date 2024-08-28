package cn.redcoral.messageplus.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务工具类
 * @author mo
 **/
public class ScheduledTaskUtil {

    private static final Map<String, ScheduledTask> map = new ConcurrentHashMap<>();


    public interface Run {
        /**
         * 运行主体
         */
        void run() throws Exception;
    }

    static class ScheduledTask {
        // 间隔时间（单位：ms）
        private long intervalTime = 100;
        // 是否执行
        private boolean isCarryOut = false;
        // 定时任务
        private Run run;

        /**
         * 定时任务
         * @param intervalTime 间隔时间（默认为ms）
         * @param run 重试内容
         */
        public ScheduledTask(long intervalTime, Run run) {
            this(intervalTime, TimeUnit.MILLISECONDS, run);
        }

        /**
         * 重试方法<p>
         * 重试需要抛出RuntimeException异常
         * @param intervalTime 间隔时间
         * @param timeUnit 时间单位
         * @param run 重试内容
         */
        public ScheduledTask(long intervalTime, TimeUnit timeUnit, Run run) {
            this.intervalTime = timeUnit.toMillis(intervalTime);
            this.run = run;
        }

        public void start() {
            // 已经正在执行
            if (this.isCarryOut) return;
            // 设置为正在执行
            this.isCarryOut = true;
            AsyncUtil.async(()->{
                while (true) {
                    // 执行任务
                    this.run.run();
                    // 暂停任务
                    if (!this.isCarryOut) {
                        return null;
                    }
                    // 间隔
                    Thread.sleep(this.intervalTime);
                }
            });
        }

        public void stop() {
            this.isCarryOut = false;
        }
    }

    public static void start(String key, long intervalTime, Run run) {
        ScheduledTask scheduledTask = map.get(key);
        if (scheduledTask == null) {
            scheduledTask = new ScheduledTask(intervalTime, run);
            map.put(key, scheduledTask);
        }
        scheduledTask.start();
    }

    public static void start(String key, long intervalTime, TimeUnit timeUnit, Run run) {
        start(key,timeUnit.toMillis(intervalTime),run);
    }

    public static void stop(String key) {
        ScheduledTask scheduledTask = map.get(key);
        if (scheduledTask == null) return;
        scheduledTask.stop();
    }

}
