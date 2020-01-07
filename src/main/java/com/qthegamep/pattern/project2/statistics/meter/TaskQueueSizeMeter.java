package com.qthegamep.pattern.project2.statistics.meter;

import com.qthegamep.pattern.project2.statistics.Meters;
import com.qthegamep.pattern.project2.util.Constants;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

public class TaskQueueSizeMeter implements MeterBinder {

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Gauge.builder("task.queue.size", Meters.TASK_QUEUE_SIZE_METER::get)
                .description("The current number of tasks in queue")
                .baseUnit(Constants.GRIZZLY)
                .register(meterRegistry);
        Gauge.builder("task.queue.limit", () -> Integer.parseInt(System.getProperty("application.server.queue.limit")))
                .description("The limit size of the task queue. If limit value is negative then queue has infinity limit")
                .baseUnit(Constants.GRIZZLY)
                .register(meterRegistry);
    }
}
