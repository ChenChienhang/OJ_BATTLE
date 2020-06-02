package com.team10.ojbattle.common.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: 陈健航
 * @description: 线程池
 * @since: 2020/5/30 17:24
 * @version: 1.0
 */
public class ThreadPool {

    private static final ExecutorService THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            4,
            40,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024),
            new ThreadFactoryBuilder().setNameFormat("test-pool-%d").build(),
            new ThreadPoolExecutor.AbortPolicy()
    );

    public static ExecutorService getInstance() {
        return THREAD_POOL_EXECUTOR;
    }
}
