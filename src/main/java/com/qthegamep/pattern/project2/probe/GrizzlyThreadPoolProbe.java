package com.qthegamep.pattern.project2.probe;

import com.qthegamep.pattern.project2.statistics.Meters;
import org.glassfish.grizzly.threadpool.AbstractThreadPool;
import org.glassfish.grizzly.threadpool.ThreadPoolProbe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrizzlyThreadPoolProbe extends ThreadPoolProbe.Adapter {

    private static final Logger LOG = LoggerFactory.getLogger(GrizzlyThreadPoolProbe.class);

    @Override
    public void onTaskDequeueEvent(AbstractThreadPool threadPool, Runnable task) {
        long availableThreads = Meters.AVAILABLE_THREADS_METER.decrementAndGet();
        LOG.debug("Available threads before task {}", availableThreads);
    }

    @Override
    public void onTaskCompleteEvent(AbstractThreadPool threadPool, Runnable task) {
        long availableThreads = Meters.AVAILABLE_THREADS_METER.incrementAndGet();
        LOG.debug("Available threads after task {}", availableThreads);
    }
}
