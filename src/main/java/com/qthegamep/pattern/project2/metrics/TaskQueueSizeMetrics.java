package com.qthegamep.pattern.project2.metrics;

import com.qthegamep.pattern.project2.config.TaskQueueSizeProbe;
import com.qthegamep.pattern.project2.util.Constants;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

import java.util.concurrent.atomic.AtomicLong;

public class TaskQueueSizeMetrics implements MeterBinder {

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        AtomicLong taskQueueSize = TaskQueueSizeProbe.TASK_QUEUE_SIZE;
        Gauge.builder(Constants.TASK_QUEUE_SIZE_METRICS.getValue(), taskQueueSize::get)
                .description("The current number of tasks in queue")
                .baseUnit(Constants.GRIZZLY.getValue())
                .register(meterRegistry);
    }
}
