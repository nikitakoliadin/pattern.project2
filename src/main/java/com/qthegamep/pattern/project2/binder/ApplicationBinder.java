package com.qthegamep.pattern.project2.binder;

import com.qthegamep.pattern.project2.metrics.GrizzlyThreadPoolMetrics;
import com.qthegamep.pattern.project2.metrics.TaskQueueSizeMetrics;
import com.qthegamep.pattern.project2.service.*;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.binder.jvm.*;
import io.micrometer.core.instrument.binder.system.*;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import org.glassfish.jersey.internal.inject.AbstractBinder;

import javax.inject.Singleton;

public class ApplicationBinder extends AbstractBinder {

    private ConverterService converterService;
    private GenerationService generationService;
    private ErrorResponseBuilderService errorResponseBuilderService;
    private PrometheusMeterRegistry prometheusMeterRegistry;

    private ApplicationBinder(ConverterService converterService, GenerationService generationService, ErrorResponseBuilderService errorResponseBuilderService, PrometheusMeterRegistry prometheusMeterRegistry) {
        this.converterService = converterService;
        this.generationService = generationService;
        this.errorResponseBuilderService = errorResponseBuilderService;
        this.prometheusMeterRegistry = prometheusMeterRegistry;
    }

    public ConverterService getConverterService() {
        return converterService;
    }

    public GenerationService getGenerationService() {
        return generationService;
    }

    public ErrorResponseBuilderService getErrorResponseBuilderService() {
        return errorResponseBuilderService;
    }

    public PrometheusMeterRegistry getPrometheusMeterRegistry() {
        return prometheusMeterRegistry;
    }

    public static ApplicationBinderBuilder builder() {
        return new ApplicationBinderBuilder();
    }

    @Override
    protected void configure() {
        bindConverterService();
        bindGenerationService();
        bindErrorResponseBuilderService();
        bindPrometheusMeterRegistry();
    }

    private void bindConverterService() {
        if (converterService == null) {
            bind(ConverterServiceImpl.class).to(ConverterService.class).in(Singleton.class);
        } else {
            bind(converterService).to(ConverterService.class).in(Singleton.class);
        }
    }

    private void bindGenerationService() {
        if (generationService == null) {
            bind(GenerationServiceImpl.class).to(GenerationService.class).in(Singleton.class);
        } else {
            bind(generationService).to(GenerationService.class).in(Singleton.class);
        }
    }

    private void bindErrorResponseBuilderService() {
        if (errorResponseBuilderService == null) {
            bind(ErrorResponseBuilderServiceImpl.class).to(ErrorResponseBuilderService.class).in(Singleton.class);
        } else {
            bind(errorResponseBuilderService).to(ErrorResponseBuilderService.class).in(Singleton.class);
        }
    }

    private void bindPrometheusMeterRegistry() {
        if (prometheusMeterRegistry == null) {
            PrometheusMeterRegistry newPrometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT, CollectorRegistry.defaultRegistry, Clock.SYSTEM);
            new ClassLoaderMetrics().bindTo(newPrometheusMeterRegistry);
            new JvmGcMetrics().bindTo(newPrometheusMeterRegistry);
            new JvmMemoryMetrics().bindTo(newPrometheusMeterRegistry);
            new JvmThreadMetrics().bindTo(newPrometheusMeterRegistry);
            new ProcessorMetrics().bindTo(newPrometheusMeterRegistry);
            new UptimeMetrics().bindTo(newPrometheusMeterRegistry);
            new TaskQueueSizeMetrics().bindTo(newPrometheusMeterRegistry);
            new GrizzlyThreadPoolMetrics().bindTo(newPrometheusMeterRegistry);
            bind(newPrometheusMeterRegistry).to(PrometheusMeterRegistry.class).in(Singleton.class);
        } else {
            bind(prometheusMeterRegistry).to(PrometheusMeterRegistry.class).in(Singleton.class);
        }
    }

    public static class ApplicationBinderBuilder {

        private ConverterService converterService;
        private GenerationService generationService;
        private ErrorResponseBuilderService errorResponseBuilderService;
        private PrometheusMeterRegistry prometheusMeterRegistry;

        public ApplicationBinderBuilder setConverterService(ConverterService converterService) {
            this.converterService = converterService;
            return this;
        }

        public ApplicationBinderBuilder setGenerationService(GenerationService generationService) {
            this.generationService = generationService;
            return this;
        }

        public ApplicationBinderBuilder errorResponseBuilderService(ErrorResponseBuilderService errorResponseBuilderService) {
            this.errorResponseBuilderService = errorResponseBuilderService;
            return this;
        }

        public ApplicationBinderBuilder setPrometheusMeterRegistry(PrometheusMeterRegistry prometheusMeterRegistry) {
            this.prometheusMeterRegistry = prometheusMeterRegistry;
            return this;
        }

        public ApplicationBinder build() {
            return new ApplicationBinder(converterService, generationService, errorResponseBuilderService, prometheusMeterRegistry);
        }
    }
}
