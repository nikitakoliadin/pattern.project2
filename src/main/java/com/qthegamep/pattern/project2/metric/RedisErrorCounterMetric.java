package com.qthegamep.pattern.project2.metric;

import com.qthegamep.pattern.project2.util.Constants;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

public class RedisErrorCounterMetric implements MeterBinder {

    private static final String REDIS_ERROR_COUNTER = "redis.error.counter";

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Gauge.builder(REDIS_ERROR_COUNTER, Metrics.REDIS_ERROR_COUNTER_METRIC::get)
                .description("The current number of Redis errors")
                .baseUnit(Constants.REDIS)
                .register(meterRegistry);
    }
}
