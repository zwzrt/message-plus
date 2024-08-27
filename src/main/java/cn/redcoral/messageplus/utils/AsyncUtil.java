package cn.redcoral.messageplus.utils;

import java.util.concurrent.*;

/**
 * 异步工具类
 * @author mo
 **/
public class AsyncUtil {

    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            10, // 核心线程数
            20,     // 最大线程数
            10,     // 空闲存活时间
            TimeUnit.SECONDS, // 时间单位
            new LinkedBlockingQueue<Runnable>(), // 工作队列
            Executors.defaultThreadFactory(), // 线程工程
            new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略
    );

    public static void async(Callable<?> callable) {
        threadPoolExecutor.submit(callable);
    }

}
