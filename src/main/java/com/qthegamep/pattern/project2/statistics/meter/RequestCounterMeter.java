package com.qthegamep.pattern.project2.statistics.meter;

import com.qthegamep.pattern.project2.statistics.Meters;
import com.qthegamep.pattern.project2.util.Constants;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;

public class RequestCounterMeter implements MeterBinder {

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Meters.REQUEST_COUNTER_METER.forEach((key, value) -> Gauge.builder("request.counter", value::get)
                .description("The request counter")
                .baseUnit(Constants.GRIZZLY)
                .tags(Tags.of("path", key))
                .register(meterRegistry));
    }
}
