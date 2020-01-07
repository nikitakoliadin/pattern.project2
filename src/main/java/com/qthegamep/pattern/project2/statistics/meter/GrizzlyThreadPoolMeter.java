package com.qthegamep.pattern.project2.statistics.meter;

import com.qthegamep.pattern.project2.statistics.Meters;
import com.qthegamep.pattern.project2.util.Constants;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

public class GrizzlyThreadPoolMeter implements MeterBinder {

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Gauge.builder("core.pool.size", () -> Integer.parseInt(System.getProperty("application.server.core.pool.size")))
                .description("The core pool size of the Grizzly server")
                .baseUnit(Constants.GRIZZLY)
                .register(meterRegistry);
        Gauge.builder("max.pool.size", () -> Integer.parseInt(System.getProperty("application.server.max.pool.size")))
                .description("The max pool size of the Grizzly server")
                .baseUnit(Constants.GRIZZLY)
                .register(meterRegistry);
        Gauge.builder("selector.runners", () -> Runtime.getRuntime().availableProcessors() * Integer.parseInt(System.getProperty("application.server.selector.runners.multiplier")))
                .description("The selector runners of the Grizzly server")
                .baseUnit(Constants.GRIZZLY)
                .register(meterRegistry);
        Gauge.builder("available.threads", Meters.AVAILABLE_GRIZZLY_THREADS_METER::get)
                .description("The current number of available threads in Grizzly thread pool")
                .baseUnit(Constants.GRIZZLY)
                .register(meterRegistry);
        Gauge.builder("worked.threads", () -> Integer.parseInt(System.getProperty("application.server.core.pool.size")) - Meters.AVAILABLE_GRIZZLY_THREADS_METER.get())
                .description("The current number of worked threads in Grizzly thread pool")
                .baseUnit(Constants.GRIZZLY)
                .register(meterRegistry);
    }
}
