package com.framework.module.common;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class StaticThreadPool {

    public static final StaticThreadPool STATIC_THREAD_POOL = new StaticThreadPool();
    private final int corePoolSize = 1000;
    private final int maximumPoolSize = 2000;
    private final int keepAliveTime = 60;
    private final int workQueueSize = 2000;

    private ThreadPoolExecutor executor;

    private StaticThreadPool() {
        super();
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingDeque<>(workQueueSize));
    }

    public static StaticThreadPool getInstance() {
        return STATIC_THREAD_POOL;
    }

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }


}
