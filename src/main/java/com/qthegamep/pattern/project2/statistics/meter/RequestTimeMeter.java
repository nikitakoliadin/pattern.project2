package com.qthegamep.pattern.project2.statistics.meter;

import com.qthegamep.pattern.project2.statistics.Meters;
import com.qthegamep.pattern.project2.util.Constants;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

public class RequestTimeMeter implements MeterBinder {

    private static final String PATH_TAG = "path";

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Meters.REQUEST_TIME_METER.forEach((key, value) -> Gauge.builder("request.time",
                () -> {
                    double result = Arrays.stream(value.toArray())
                            .mapToLong(num -> ((AtomicLong) num).get())
                            .average()
                            .orElse(0.0);
                    value.clear();
                    return Math.round(result);
                })
                .description("The request time")
                .baseUnit(Constants.GRIZZLY)
                .tags(Tags.of(PATH_TAG, key))
                .register(meterRegistry));
        Meters.MAX_REQUEST_TIME_METER.forEach((key, value) -> Gauge.builder("max.request.time",
                () -> {
                    long result = Arrays.stream(value.toArray())
                            .mapToLong(num -> ((AtomicLong) num).get())
                            .max()
                            .orElse(0);
                    value.clear();
                    return result;
                })
                .description("The max request time")
                .baseUnit(Constants.GRIZZLY)
                .tags(Tags.of(PATH_TAG, key))
                .register(meterRegistry));
    }
}
