package cn.redcoral.messageplus.utils;

import java.util.concurrent.TimeUnit;

/**
 * 重试工具类
 * @author mo
 **/
public class RetryUtil {

    public interface Run {
        /**
         * 运行主体
         */
        void run() throws Exception;
    }
    
    public interface Finish {
        /**
         * 运行完成后的方法
         * @param bo 重试是否成功
         */
        void finish(boolean bo);
    }

    /**
     * 重试方法<p>
     * 重试需要抛出RuntimeException异常
     * @param retryTimes 重试次数
     * @param retryInterval 重试间隔（默认为ms）
     * @param run 重试内容
     */
    public static void retry(int retryTimes, long retryInterval, Run run, Finish finish) {
        retry(retryTimes, retryInterval, TimeUnit.MILLISECONDS, run, finish);
    }

    /**
     * 重试方法<p>
     * 重试需要抛出RuntimeException异常
     * @param retryTimes 重试次数
     * @param retryInterval 重试间隔
     * @param timeUnit 时间单位
     * @param run 重试内容
     */
    public static void retry(int retryTimes, long retryInterval, TimeUnit timeUnit, Run run, Finish finish) {
        AsyncUtil.async(()->{
            for (int i = 0; i < retryTimes;) {
                try {
                    run.run();
                    // 执行成功
                    finish.finish(true);
                    return null;
                } catch (RuntimeException e) {
                    // 进行下一次重试
                    i++;
                    // 间隔指定时间
                    Thread.sleep(timeUnit.toMillis(retryInterval));
                }
            }
            // 执行失败
            finish.finish(false);
            return null;
        });
    }

}
