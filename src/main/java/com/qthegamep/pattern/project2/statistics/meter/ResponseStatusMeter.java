package com.qthegamep.pattern.project2.statistics.meter;

import com.qthegamep.pattern.project2.statistics.Meters;
import com.qthegamep.pattern.project2.util.Constants;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;

public class ResponseStatusMeter implements MeterBinder {

    private static final String RESPONSE_STATUS = "response.status";
    private static final String RESPONSE_CODE_TAG = "response.code";

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Meters.RESPONSE_STATUS_METER.forEach((key, value) -> Gauge.builder(RESPONSE_STATUS, value::get)
                .description("The response status")
                .baseUnit(Constants.GRIZZLY)
                .tags(Tags.of(RESPONSE_CODE_TAG, key))
                .register(meterRegistry));
    }
}
