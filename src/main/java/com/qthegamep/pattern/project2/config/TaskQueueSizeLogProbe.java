package com.qthegamep.pattern.project2.config;

import org.glassfish.grizzly.threadpool.AbstractThreadPool;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;
import org.glassfish.grizzly.threadpool.ThreadPoolProbe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;

public class TaskQueueSizeLogProbe extends ThreadPoolProbe.Adapter {

    private static final Logger LOG = LoggerFactory.getLogger(TaskQueueSizeLogProbe.class);

    @Override
    public void onTaskQueueEvent(AbstractThreadPool threadPool, Runnable task) {
        ThreadPoolConfig threadPoolConfig = threadPool.getConfig();
        Queue<Runnable> taskQueue = threadPoolConfig.getQueue();
        LOG.info("--->>> New task in queue. Queue size: {}", taskQueue.size());
    }
}
