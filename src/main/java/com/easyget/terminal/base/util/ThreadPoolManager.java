package com.easyget.terminal.base.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {
    
    private final ExecutorService fixed_executor = Executors.newFixedThreadPool(10);
    
    private final ScheduledExecutorService scheduled_executor = Executors.newScheduledThreadPool(10);
    
    private static ThreadPoolManager ISTANCE;
    
    public static synchronized ThreadPoolManager getInstance() {
        if (ISTANCE == null) {
            ISTANCE = new ThreadPoolManager();
        }
        return ISTANCE;
    }

    /**
     * 
     * @param command  the task to execute
     * @param initialDelay 延迟时间
     * @param delay 周期
     * @param unit 单位
     * @return
     */
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
                                                     long initialDelay,
                                                     long delay,
                                                     TimeUnit unit) {
        return scheduled_executor.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    public void execute(Runnable command) {
        fixed_executor.execute(command);
    }

    public void shutdown() {
        fixed_executor.shutdownNow();
        scheduled_executor.shutdownNow();
    }
}