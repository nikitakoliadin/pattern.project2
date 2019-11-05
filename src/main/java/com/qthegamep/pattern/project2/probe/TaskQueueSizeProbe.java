package com.qthegamep.pattern.project2.probe;

import com.qthegamep.pattern.project2.metric.Metrics;
import org.glassfish.grizzly.threadpool.AbstractThreadPool;
import org.glassfish.grizzly.threadpool.ThreadPoolProbe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;

public class TaskQueueSizeProbe extends ThreadPoolProbe.Adapter {

    private static final Logger LOG = LoggerFactory.getLogger(TaskQueueSizeProbe.class);

    @Override
    public void onTaskQueueEvent(AbstractThreadPool threadPool, Runnable task) {
        Queue<Runnable> taskQueue = threadPool.getQueue();
        LOG.info("--->>> New task in queue. Task queue size: {}", taskQueue.size());
        Metrics.TASK_QUEUE_SIZE_METRIC.set(taskQueue.size());
        LOG.debug("Atomic task queue size: {}", Metrics.TASK_QUEUE_SIZE_METRIC.get());
    }

    @Override
    public void onTaskDequeueEvent(AbstractThreadPool threadPool, Runnable task) {
        Queue<Runnable> taskQueue = threadPool.getQueue();
        LOG.info("--->>> Task pooled from queue. Task queue size: {}", taskQueue.size());
        Metrics.TASK_QUEUE_SIZE_METRIC.set(taskQueue.size());
        LOG.debug("Atomic task queue size: {}", Metrics.TASK_QUEUE_SIZE_METRIC.get());
    }
}
