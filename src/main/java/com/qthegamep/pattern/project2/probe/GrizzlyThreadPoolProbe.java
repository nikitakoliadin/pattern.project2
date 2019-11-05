package com.qthegamep.pattern.project2.probe;

import com.qthegamep.pattern.project2.metric.Metrics;
import org.glassfish.grizzly.threadpool.AbstractThreadPool;
import org.glassfish.grizzly.threadpool.ThreadPoolProbe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrizzlyThreadPoolProbe extends ThreadPoolProbe.Adapter {

    private static final Logger LOG = LoggerFactory.getLogger(GrizzlyThreadPoolProbe.class);

    @Override
    public void onTaskDequeueEvent(AbstractThreadPool threadPool, Runnable task) {
        long availableThreads = Metrics.AVAILABLE_THREADS_METRIC.decrementAndGet();
        LOG.debug("Available threads before task {}", availableThreads);
    }

    @Override
    public void onTaskCompleteEvent(AbstractThreadPool threadPool, Runnable task) {
        long availableThreads = Metrics.AVAILABLE_THREADS_METRIC.incrementAndGet();
        LOG.debug("Available threads after task {}", availableThreads);
    }
}
