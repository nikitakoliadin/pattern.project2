package com.qthegamep.pattern.project2.metric;

import java.util.concurrent.atomic.AtomicLong;

public class Metrics {

    public static final AtomicLong TASK_QUEUE_SIZE_METRIC = new AtomicLong();

    public static final AtomicLong AVAILABLE_THREADS_METRIC = new AtomicLong(Integer.parseInt(System.getProperty("server.core.pool.size")));
}
