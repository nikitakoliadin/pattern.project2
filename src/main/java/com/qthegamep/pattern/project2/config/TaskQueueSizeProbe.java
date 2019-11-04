package com.qthegamep.pattern.project2.config;

import org.glassfish.grizzly.threadpool.AbstractThreadPool;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;
import org.glassfish.grizzly.threadpool.ThreadPoolProbe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

public class TaskQueueSizeProbe extends ThreadPoolProbe.Adapter {

    private static final Logger LOG = LoggerFactory.getLogger(TaskQueueSizeProbe.class);

    public static final AtomicLong TASK_QUEUE_SIZE = new AtomicLong();

    @Override
    public void onTaskQueueEvent(AbstractThreadPool threadPool, Runnable task) {
        ThreadPoolConfig threadPoolConfig = threadPool.getConfig();
        Queue<Runnable> taskQueue = threadPoolConfig.getQueue();
        LOG.info("--->>> New task in queue. Task queue size: {}", taskQueue.size());
        TASK_QUEUE_SIZE.set(taskQueue.size());
        LOG.debug("Atomic task queue size: {}", TASK_QUEUE_SIZE.get());
    }
}
