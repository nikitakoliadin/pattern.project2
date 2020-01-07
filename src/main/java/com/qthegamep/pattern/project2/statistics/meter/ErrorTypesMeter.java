package com.qthegamep.pattern.project2.statistics.meter;

import com.qthegamep.pattern.project2.statistics.Meters;
import com.qthegamep.pattern.project2.util.Constants;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;

public class ErrorTypesMeter implements MeterBinder {

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Meters.ERROR_TYPES_METER.forEach((key, value) -> Gauge.builder("error.types", value::get)
                .description("The error response types")
                .baseUnit(Constants.GRIZZLY)
                .tags(Tags.of("error.code", key))
                .register(meterRegistry));
    }
}
