package com.qthegamep.pattern.project2.metric;

import com.qthegamep.pattern.project2.util.Constants;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

public class RequestTimeMetric implements MeterBinder {

    private static final String REQUEST_TIME = "request.time";
    private static final String PATH_TAG = "path";

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Metrics.REQUEST_TIME_METRIC.forEach((key, value) -> Gauge.builder(REQUEST_TIME,
                () -> {
                    double result = Arrays.stream(value.toArray())
                            .mapToLong(num -> ((AtomicLong) num).get())
                            .average()
                            .orElse(0.0);
                    value.clear();
                    return Math.round(result);
                })
                .description("The request time")
                .baseUnit(Constants.GRIZZLY.getValue())
                .tags(Tags.of(PATH_TAG, key))
                .register(meterRegistry));
    }
}
