package com.qthegamep.pattern.project2.statistics;

import com.qthegamep.pattern.project2.model.container.Error;
import com.qthegamep.pattern.project2.util.Paths;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Meters {

    public static final AtomicLong TASK_QUEUE_SIZE_METER = new AtomicLong();

    public static final AtomicLong AVAILABLE_GRIZZLY_THREADS_METER = new AtomicLong(Integer.parseInt(System.getProperty("application.server.core.pool.size")));

    public static final Map<String, AtomicLong> ERROR_TYPES_METER = Arrays.stream(Error.values())
            .collect(Collectors.toMap(
                    value -> String.valueOf(value.getErrorCode()),
                    value -> new AtomicLong(),
                    (key, value) -> value,
                    ConcurrentHashMap::new));

    public static final Map<String, AtomicLong> RESPONSE_STATUS_METER = Arrays.stream(Response.Status.values())
            .collect(Collectors.toMap(
                    value -> String.valueOf(value.getStatusCode()),
                    value -> new AtomicLong(),
                    (key, value) -> value,
                    ConcurrentHashMap::new));

    public static final Map<String, AtomicLong> REQUEST_COUNTER_METER = Arrays.stream(Paths.class.getFields())
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
                    (key, value) -> value,
                    ConcurrentHashMap::new));

    public static final Map<String, List<AtomicLong>> AVERAGE_REQUEST_TIME_METER = Arrays.stream(Paths.class.getFields())
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
                    (key, value) -> value,
                    ConcurrentHashMap::new));

    public static final Map<String, List<AtomicLong>> MAX_REQUEST_TIME_METER = Arrays.stream(Paths.class.getFields())
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

    public static final AtomicLong REDIS_ERROR_COUNTER_METER = new AtomicLong();

    private Meters() {
    }
}
