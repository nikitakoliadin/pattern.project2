package com.qthegamep.pattern.project2.statistics.meter;

import com.qthegamep.pattern.project2.statistics.Meters;
import com.qthegamep.pattern.project2.util.Constants;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

public class RedisErrorCounterMeter implements MeterBinder {

    private static final String REDIS_ERROR_COUNTER = "redis.error.counter";

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Gauge.builder(REDIS_ERROR_COUNTER, Meters.REDIS_ERROR_COUNTER_METER::get)
                .description("The current number of Redis errors")
                .baseUnit(Constants.REDIS)
                .register(meterRegistry);
    }
}
