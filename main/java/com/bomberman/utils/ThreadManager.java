package com.bomberman.utils;

import java.util.concurrent.*;

public class ThreadManager {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static ScheduledFuture<?> schedule(Runnable task, long delay, TimeUnit unit) {
        return scheduler.schedule(task, delay, unit);
    }

    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        return scheduler.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    public static Future<?> execute(Runnable task) {
        return executor.submit(task);
    }

    public static void shutdown() {
        scheduler.shutdown();
        executor.shutdown();
    }
}