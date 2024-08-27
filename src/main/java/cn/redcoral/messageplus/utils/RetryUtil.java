package cn.redcoral.messageplus.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 重试工具类
 * @author mo
 **/
public class RetryUtil {

    public interface Run {
        void run() throws Exception;
    }

    /**
     * 重试方法<p>
     * 重试需要抛出RuntimeException异常
     * @param retryTimes 重试次数
     * @param retryInterval 重试间隔（默认为ms）
     * @param run 重试内容
     */
    public static void retry(int retryTimes, int retryInterval, Run run) {
        retry(retryTimes, retryInterval, TimeUnit.MILLISECONDS, run);
    }

    /**
     * 重试方法<p>
     * 重试需要抛出RuntimeException异常
     * @param retryTimes 重试次数
     * @param retryInterval 重试间隔
     * @param timeUnit 时间单位
     * @param callable 重试内容
     */
    public static void retry(int retryTimes, int retryInterval, TimeUnit timeUnit, Run run) {
        AsyncUtil.async(()->{
            for (int i = 0; i < retryTimes;) {
                try {
                    run.run();
                    break;
                } catch (RuntimeException e) {
                    // 进行下一次重试
                    i++;
                    // 间隔指定时间
                    Thread.sleep(timeUnit.toMillis(retryInterval));
                }
            }
            return null;
        });
    }

}
