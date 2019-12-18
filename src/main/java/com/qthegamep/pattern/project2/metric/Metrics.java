package com.qthegamep.pattern.project2.metric;

import com.qthegamep.pattern.project2.model.ErrorType;
import com.qthegamep.pattern.project2.util.Paths;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Metrics {

    public static final AtomicLong TASK_QUEUE_SIZE_METRIC = new AtomicLong();

    public static final AtomicLong AVAILABLE_THREADS_METRIC = new AtomicLong(Integer.parseInt(System.getProperty("application.server.core.pool.size")));

    public static final Map<String, AtomicLong> ERROR_TYPES_METRIC = Arrays.stream(ErrorType.values())
            .collect(Collectors.toMap(
                    value -> String.valueOf(value.getErrorCode()),
                    value -> new AtomicLong(),
                    (a, b) -> b,
                    ConcurrentHashMap::new));

    public static final Map<String, AtomicLong> RESPONSE_STATUS_METRIC = Arrays.stream(Response.Status.values())
            .collect(Collectors.toMap(
                    value -> String.valueOf(value.getStatusCode()),
                    value -> new AtomicLong(),
                    (a, b) -> b,
                    ConcurrentHashMap::new));

    public static final Map<String, AtomicLong> REQUEST_COUNTER_METRIC = Arrays.stream(Paths.class.getFields())
            .map(field -> {
                try {
                    return String.valueOf(field.get(null));
                } catch (IllegalAccessException e) {
                    return null;
                }
            })
            .collect(Collectors.toMap(
                    value -> value,
                    value -> new AtomicLong(),
                    (a, b) -> b,
                    ConcurrentHashMap::new));

    public static final Map<String, List<AtomicLong>> REQUEST_TIME_METRIC = Arrays.stream(Paths.class.getFields())
            .map(field -> {
                try {
                    return String.valueOf(field.get(null));
                } catch (IllegalAccessException e) {
                    return null;
                }
            })
            .collect(Collectors.toMap(
                    value -> value,
                    value -> new CopyOnWriteArrayList<>(),
                    (a, b) -> b,
                    ConcurrentHashMap::new));

    public static final Map<String, List<AtomicLong>> MAX_REQUEST_TIME_METRIC = Arrays.stream(Paths.class.getFields())
            .map(field -> {
                try {
                    return String.valueOf(field.get(null));
                } catch (IllegalAccessException e) {
                    return null;
                }
            })
            .collect(Collectors.toMap(
                    value -> value,
                    value -> new CopyOnWriteArrayList<>(),
                    (a, b) -> b,
                    ConcurrentHashMap::new));

    private Metrics() {
    }
}
