package com.qthegamep.pattern.project2.metric;

import com.qthegamep.pattern.project2.util.Constants;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;
import sun.management.Util;

import java.lang.management.PlatformManagedObject;

public class RequestCounterMetric implements MeterBinder {

    private static final String REQUEST_COUNTER = "request.counter";
    private static final String PATH_TAG = "path";

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        PlatformManagedObject errorTypesPlatformManagedObject = () -> Util.newObjectName(REQUEST_COUNTER);
        Metrics.REQUEST_COUNTER_METRIC.forEach((key, value) -> Gauge.builder(REQUEST_COUNTER, errorTypesPlatformManagedObject, platformManagedObject -> value.get())
                .description("The request counter")
                .baseUnit(Constants.GRIZZLY.getValue())
                .tags(Tags.of(PATH_TAG, key))
                .register(meterRegistry));
    }
}
