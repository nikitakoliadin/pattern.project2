package com.qthegamep.pattern.project2.metric;

import com.qthegamep.pattern.project2.util.Constants;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

public class GrizzlyThreadPoolMetric implements MeterBinder {

    private static final String CORE_POOL_SIZE = "core.pool.size";
    private static final String MAX_POOL_SIZE = "max.pool.size";
    private static final String SELECTOR_RUNNERS = "selector.runners";
    private static final String AVAILABLE_THREADS = "available.threads";
    private static final String WORKED_THREADS = "worked.threads";

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Gauge.builder(CORE_POOL_SIZE, () -> Integer.parseInt(System.getProperty("server.core.pool.size")))
                .description("The core pool size of the Grizzly server")
                .baseUnit(Constants.GRIZZLY.getValue())
                .register(meterRegistry);
        Gauge.builder(MAX_POOL_SIZE, () -> Integer.parseInt(System.getProperty("server.max.pool.size")))
                .description("The max pool size of the Grizzly server")
                .baseUnit(Constants.GRIZZLY.getValue())
                .register(meterRegistry);
        Gauge.builder(SELECTOR_RUNNERS, () -> Runtime.getRuntime().availableProcessors() * Integer.parseInt(System.getProperty("server.selector.runners.multiplier")))
                .description("The selector runners of the Grizzly server")
                .baseUnit(Constants.GRIZZLY.getValue())
                .register(meterRegistry);
        Gauge.builder(AVAILABLE_THREADS, Metrics.AVAILABLE_THREADS_METRIC::get)
                .description("The current number of available threads in Grizzly thread pool")
                .baseUnit(Constants.GRIZZLY.getValue())
                .register(meterRegistry);
        Gauge.builder(WORKED_THREADS, () -> Integer.parseInt(System.getProperty("server.core.pool.size")) - Metrics.AVAILABLE_THREADS_METRIC.get())
                .description("The current number of worked threads in Grizzly thread pool")
                .baseUnit(Constants.GRIZZLY.getValue())
                .register(meterRegistry);
    }
}
