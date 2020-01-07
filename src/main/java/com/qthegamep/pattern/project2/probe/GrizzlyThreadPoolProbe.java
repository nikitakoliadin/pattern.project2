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
        long availableGrizzlyThreads = Meters.AVAILABLE_GRIZZLY_THREADS_METER.decrementAndGet();
        LOG.debug("Available Grizzly threads before task {}", availableGrizzlyThreads);
    }

    @Override
    public void onTaskCompleteEvent(AbstractThreadPool threadPool, Runnable task) {
        long availableGrizzlyThreads = Meters.AVAILABLE_GRIZZLY_THREADS_METER.incrementAndGet();
        LOG.debug("Available Grizzly threads after task {}", availableGrizzlyThreads);
    }
}
