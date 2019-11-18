package com.qthegamep.pattern.project2.binder;

import com.qthegamep.pattern.project2.metric.*;
import com.qthegamep.pattern.project2.service.*;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.binder.jvm.*;
import io.micrometer.core.instrument.binder.mongodb.MongoMetricsCommandListener;
import io.micrometer.core.instrument.binder.mongodb.MongoMetricsConnectionPoolListener;
import io.micrometer.core.instrument.binder.system.*;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

public class ApplicationBinder extends AbstractBinder {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationBinder.class);

    private ConverterService converterService;
    private GenerationService generationService;
    private ErrorResponseBuilderService errorResponseBuilderService;
    private PrometheusMeterRegistry prometheusMeterRegistry;
    private DatabaseConnector databaseConnector;
    private com.mongodb.client.MongoDatabase syncMongoDatabase;

    private MongoMetricsCommandListener mongoMetricsCommandListener;
    private MongoMetricsConnectionPoolListener mongoMetricsConnectionPoolListener;

    private ApplicationBinder(ConverterService converterService, GenerationService generationService, ErrorResponseBuilderService errorResponseBuilderService, PrometheusMeterRegistry prometheusMeterRegistry, DatabaseConnector databaseConnector, com.mongodb.client.MongoDatabase syncMongoDatabase) {
        this.converterService = converterService;
        this.generationService = generationService;
        this.errorResponseBuilderService = errorResponseBuilderService;
        this.prometheusMeterRegistry = prometheusMeterRegistry;
        this.databaseConnector = databaseConnector;
        this.syncMongoDatabase = syncMongoDatabase;
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

    public DatabaseConnector getDatabaseConnector() {
        return databaseConnector;
    }

    public com.mongodb.client.MongoDatabase getSyncMongoDatabase() {
        return syncMongoDatabase;
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
        bindDatabaseConnector();
        bindSyncMongoDatabase();
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
            new TaskQueueSizeMetric().bindTo(newPrometheusMeterRegistry);
            new GrizzlyThreadPoolMetric().bindTo(newPrometheusMeterRegistry);
            new ErrorTypesMetric().bindTo(newPrometheusMeterRegistry);
            new ResponseStatusMetric().bindTo(newPrometheusMeterRegistry);
            new RequestCounterMetric().bindTo(newPrometheusMeterRegistry);
            new RequestTimeMetric().bindTo(newPrometheusMeterRegistry);
            mongoMetricsCommandListener = new MongoMetricsCommandListener(newPrometheusMeterRegistry);
            mongoMetricsConnectionPoolListener = new MongoMetricsConnectionPoolListener(newPrometheusMeterRegistry);
            bind(newPrometheusMeterRegistry).to(PrometheusMeterRegistry.class).in(Singleton.class);
        } else {
            bind(prometheusMeterRegistry).to(PrometheusMeterRegistry.class).in(Singleton.class);
        }
    }

    private void bindDatabaseConnector() {
        if (databaseConnector == null) {
            databaseConnector = new DatabaseConnectorImpl();
            bind(databaseConnector).to(DatabaseConnector.class).in(Singleton.class);
        } else {
            bind(databaseConnector).to(DatabaseConnector.class).in(Singleton.class);
        }
    }

    private void bindSyncMongoDatabase() {
        if (syncMongoDatabase == null) {
            String syncMongoDbEnabled = System.getProperty("sync.mongodb.enabled");
            LOG.debug("Sync MongoDB enabled: {}", syncMongoDbEnabled);
            if (Boolean.parseBoolean(syncMongoDbEnabled)) {
                String syncMongoDbType = System.getProperty("sync.mongodb.type");
                LOG.info("Sync MongoDB type: {}", syncMongoDbType);
                com.mongodb.client.MongoDatabase syncMongoDatabase = databaseConnector.connectToSyncMongoDB(mongoMetricsCommandListener, mongoMetricsConnectionPoolListener, syncMongoDbType);
                bind(syncMongoDatabase).to(com.mongodb.client.MongoDatabase.class).in(Singleton.class);
            } else {
                LOG.warn("Sync MongoDB disabled");
            }
        } else {
            bind(syncMongoDatabase).to(com.mongodb.client.MongoDatabase.class).in(Singleton.class);
        }
    }

    public static class ApplicationBinderBuilder {

        private ConverterService converterService;
        private GenerationService generationService;
        private ErrorResponseBuilderService errorResponseBuilderService;
        private PrometheusMeterRegistry prometheusMeterRegistry;
        private DatabaseConnector databaseConnector;
        private com.mongodb.client.MongoDatabase syncMongoDatabase;

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

        public ApplicationBinderBuilder setDatabaseConnector(DatabaseConnector databaseConnector) {
            this.databaseConnector = databaseConnector;
            return this;
        }

        public ApplicationBinderBuilder setSyncMongoDatabase(com.mongodb.client.MongoDatabase syncMongoDatabase) {
            this.syncMongoDatabase = syncMongoDatabase;
            return this;
        }

        public ApplicationBinder build() {
            return new ApplicationBinder(converterService, generationService, errorResponseBuilderService, prometheusMeterRegistry, databaseConnector, syncMongoDatabase);
        }
    }
}
