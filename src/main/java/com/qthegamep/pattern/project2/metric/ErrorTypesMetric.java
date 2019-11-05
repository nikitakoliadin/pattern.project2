package com.qthegamep.pattern.project2.metric;

import com.qthegamep.pattern.project2.util.Constants;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;
import sun.management.Util;

import java.lang.management.PlatformManagedObject;

public class ErrorTypesMetric implements MeterBinder {

    private static final String ERROR_TYPES = "error.types";
    private static final String ERROR_CODE_TAG = "error.code";

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        PlatformManagedObject errorTypesPlatformManagedObject = () -> Util.newObjectName(ERROR_TYPES);
        Metrics.ERROR_TYPES_METRIC.forEach((key, value) -> Gauge.builder(ERROR_TYPES, errorTypesPlatformManagedObject, platformManagedObject -> value.get())
                .description("The error response types")
                .baseUnit(Constants.GRIZZLY.getValue())
                .tags(Tags.of(ERROR_CODE_TAG, key))
                .register(meterRegistry));
    }
}
