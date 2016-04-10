package com.tomoima.fetchtweet.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by tomoaki on 4/9/16.
 */
public class TaskRunnerThread {
    private final ThreadPoolExecutor threadPoolExecutor;
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private final BlockingQueue<Runnable> poolWorkQueue;

    public TaskRunnerThread(){
        poolWorkQueue = new LinkedBlockingQueue<>();
        threadPoolExecutor = new ThreadPoolExecutor(
                NUMBER_OF_CORES,
                NUMBER_OF_CORES,
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                poolWorkQueue
        );
    }

    public ThreadPoolExecutor getThreadPoolExecutor(){
        return threadPoolExecutor;
    }
}
