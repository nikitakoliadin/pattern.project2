package com.qthegamep.pattern.project2.metric;

import com.qthegamep.pattern.project2.model.ErrorType;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Metrics {

    public static final AtomicLong TASK_QUEUE_SIZE_METRIC = new AtomicLong();

    public static final AtomicLong AVAILABLE_THREADS_METRIC = new AtomicLong(Integer.parseInt(System.getProperty("server.core.pool.size")));

    public static final Map<String, AtomicLong> ERROR_TYPES_METRIC = Arrays.stream(ErrorType.values())
            .collect(Collectors.toMap(value -> String.valueOf(value.getErrorCode()), value -> new AtomicLong(), (a, b) -> b, ConcurrentHashMap::new));

    private Metrics() {
    }
}
