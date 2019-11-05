package com.qthegamep.pattern.project2.metrics;

import com.qthegamep.pattern.project2.util.Constants;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

public class TaskQueueSizeMetrics implements MeterBinder {

    private static final String TASK_QUEUE_SIZE = "task.queue.size";
    private static final String TASK_QUEUE_LIMIT = "task.queue.limit";

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Gauge.builder(TASK_QUEUE_SIZE, Metrics.TASK_QUEUE_SIZE_METRIC::get)
                .description("The current number of tasks in queue")
                .baseUnit(Constants.GRIZZLY.getValue())
                .register(meterRegistry);
        Gauge.builder(TASK_QUEUE_LIMIT, () -> Integer.parseInt(System.getProperty("server.queue.limit")))
                .description("The limit size of the task queue. If limit value is negative then queue has infinity limit")
                .baseUnit(Constants.GRIZZLY.getValue())
                .register(meterRegistry);
    }
}
