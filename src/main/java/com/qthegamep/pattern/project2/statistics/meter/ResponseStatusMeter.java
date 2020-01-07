package com.qthegamep.pattern.project2.statistics.meter;

import com.qthegamep.pattern.project2.statistics.Meters;
import com.qthegamep.pattern.project2.util.Constants;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;

public class ResponseStatusMeter implements MeterBinder {

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Meters.RESPONSE_STATUS_METER.forEach((key, value) -> Gauge.builder("response.status", value::get)
                .description("The response status")
                .baseUnit(Constants.GRIZZLY)
                .tags(Tags.of("response.code", key))
                .register(meterRegistry));
    }
}
