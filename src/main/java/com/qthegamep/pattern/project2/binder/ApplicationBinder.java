package com.qthegamep.pattern.project2.binder;

import com.qthegamep.pattern.project2.exception.RedisBinderRuntimeException;
import com.qthegamep.pattern.project2.metric.*;
import com.qthegamep.pattern.project2.model.ErrorType;
import com.qthegamep.pattern.project2.repository.*;
import com.qthegamep.pattern.project2.service.*;
import com.qthegamep.pattern.project2.util.Constants;
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
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import javax.inject.Singleton;

public class ApplicationBinder extends AbstractBinder {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationBinder.class);

    private ConverterService converterService;
    private GenerationService generationService;
    private ErrorResponseBuilderService errorResponseBuilderService;
    private PrometheusMeterRegistry prometheusMeterRegistry;
    private DatabaseConnectorService databaseConnectorService;
    private com.mongodb.client.MongoDatabase syncMongoDatabase;
    private com.mongodb.async.client.MongoDatabase asyncMongoDatabase;
    private JedisPool jedisPool;
    private JedisCluster jedisCluster;
    private SyncMongoRepository syncMongoRepository;
    private AsyncMongoRepository asyncMongoRepository;
    private RedisRepository redisRepository;
    private CryptoService cryptoService;
    private KeyBuilder keyBuilder;

    private MongoMetricsCommandListener mongoMetricsCommandListener;
    private MongoMetricsConnectionPoolListener mongoMetricsConnectionPoolListener;

    private ApplicationBinder(ConverterService converterService, GenerationService generationService, ErrorResponseBuilderService errorResponseBuilderService, PrometheusMeterRegistry prometheusMeterRegistry, DatabaseConnectorService databaseConnectorService, com.mongodb.client.MongoDatabase syncMongoDatabase, com.mongodb.async.client.MongoDatabase asyncMongoDatabase, JedisPool jedisPool, JedisCluster jedisCluster, SyncMongoRepository syncMongoRepository, AsyncMongoRepository asyncMongoRepository, RedisRepository redisRepository, CryptoService cryptoService, KeyBuilder keyBuilder) {
        this.converterService = converterService;
        this.generationService = generationService;
        this.errorResponseBuilderService = errorResponseBuilderService;
        this.prometheusMeterRegistry = prometheusMeterRegistry;
        this.databaseConnectorService = databaseConnectorService;
        this.syncMongoDatabase = syncMongoDatabase;
        this.asyncMongoDatabase = asyncMongoDatabase;
        this.jedisPool = jedisPool;
        this.jedisCluster = jedisCluster;
        this.syncMongoRepository = syncMongoRepository;
        this.asyncMongoRepository = asyncMongoRepository;
        this.redisRepository = redisRepository;
        this.cryptoService = cryptoService;
        this.keyBuilder = keyBuilder;
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

    public DatabaseConnectorService getDatabaseConnectorService() {
        return databaseConnectorService;
    }

    public com.mongodb.client.MongoDatabase getSyncMongoDatabase() {
        return syncMongoDatabase;
    }

    public com.mongodb.async.client.MongoDatabase getAsyncMongoDatabase() {
        return asyncMongoDatabase;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public SyncMongoRepository getSyncMongoRepository() {
        return syncMongoRepository;
    }

    public AsyncMongoRepository getAsyncMongoRepository() {
        return asyncMongoRepository;
    }

    public RedisRepository getRedisRepository() {
        return redisRepository;
    }

    public CryptoService getCryptoService() {
        return cryptoService;
    }

    public KeyBuilder getKeyBuilder() {
        return keyBuilder;
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
        bindAsyncMongoDatabase();
        bindJedisPool();
        bindJedisCluster();
        bindSyncMongoRepository();
        bindAsyncMongoRepository();
        bindRedisRepository();
        bindCryptoService();
        bindKeyBuilder();
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
        if (databaseConnectorService == null) {
            databaseConnectorService = new DatabaseConnectorServiceImpl();
            bind(databaseConnectorService).to(DatabaseConnectorService.class).in(Singleton.class);
        } else {
            bind(databaseConnectorService).to(DatabaseConnectorService.class).in(Singleton.class);
        }
    }

    private void bindSyncMongoDatabase() {
        if (syncMongoDatabase == null) {
            String syncMongoDbEnabled = System.getProperty("sync.mongodb.enabled");
            LOG.debug("Sync MongoDB enabled: {}", syncMongoDbEnabled);
            if (Boolean.parseBoolean(syncMongoDbEnabled)) {
                String syncMongoDbType = System.getProperty("sync.mongodb.type");
                LOG.info("Sync MongoDB type: {}", syncMongoDbType);
                com.mongodb.client.MongoDatabase newSyncMongoDatabase = databaseConnectorService.connectToSyncMongoDB(mongoMetricsCommandListener, mongoMetricsConnectionPoolListener, syncMongoDbType);
                bind(newSyncMongoDatabase).to(com.mongodb.client.MongoDatabase.class).in(Singleton.class);
            } else {
                LOG.warn("Sync MongoDB disabled! Don't use or remove SyncMongoDatabase binding!");
            }
        } else {
            bind(syncMongoDatabase).to(com.mongodb.client.MongoDatabase.class).in(Singleton.class);
        }
    }

    private void bindAsyncMongoDatabase() {
        if (asyncMongoDatabase == null) {
            String asyncMongoDbEnabled = System.getProperty("async.mongodb.enabled");
            LOG.debug("Async MongoDB enabled: {}", asyncMongoDbEnabled);
            if (Boolean.parseBoolean(asyncMongoDbEnabled)) {
                String asyncMongoDbType = System.getProperty("async.mongodb.type");
                LOG.info("Async MongoDB type: {}", asyncMongoDbType);
                com.mongodb.async.client.MongoDatabase newAsyncMongoDatabase = databaseConnectorService.connectToAsyncMongoDB(mongoMetricsCommandListener, mongoMetricsConnectionPoolListener, asyncMongoDbType);
                bind(newAsyncMongoDatabase).to(com.mongodb.async.client.MongoDatabase.class).to(Singleton.class);
            } else {
                LOG.warn("Async MongoDB disabled! Don't use or remove AsyncMongoDatabase binding!");
            }
        } else {
            bind(asyncMongoDatabase).to(com.mongodb.async.client.MongoDatabase.class).in(Singleton.class);
        }
    }

    private void bindJedisPool() {
        if (jedisPool == null) {
            String redisPoolEnabled = System.getProperty("redis.pool.enabled");
            LOG.debug("Redis pool enabled: {}", redisPoolEnabled);
            if (Boolean.parseBoolean(redisPoolEnabled)) {
                JedisPool newJedisPool = databaseConnectorService.connectToPoolRedis();
                bind(newJedisPool).to(JedisPool.class).in(Singleton.class);
            } else {
                LOG.warn("Redis pool disabled! Don't use or remove JedisPool binding!");
            }
        } else {
            bind(jedisPool).to(JedisPool.class).in(Singleton.class);
        }
    }

    private void bindJedisCluster() {
        if (jedisCluster == null) {
            String redisClusterEnabled = System.getProperty("redis.cluster.enabled");
            LOG.debug("Redis cluster enabled: {}", redisClusterEnabled);
            if (Boolean.parseBoolean(redisClusterEnabled)) {
                JedisCluster newJedisCluster = databaseConnectorService.connectToClusterRedis();
                bind(newJedisCluster).to(JedisCluster.class).in(Singleton.class);
            } else {
                LOG.warn("Redis cluster disabled! Don't use or remove JedisCluster binding!");
            }
        } else {
            bind(jedisCluster).to(JedisCluster.class).in(Singleton.class);
        }
    }

    private void bindSyncMongoRepository() {
        if (syncMongoRepository == null) {
            bind(SyncMongoRepositoryImpl.class).to(SyncMongoRepository.class).in(Singleton.class);
        } else {
            bind(syncMongoRepository).to(SyncMongoRepository.class).in(Singleton.class);
        }
    }

    private void bindAsyncMongoRepository() {
        if (asyncMongoRepository == null) {
            bind(AsyncMongoRepositoryImpl.class).to(AsyncMongoRepository.class).in(Singleton.class);
        } else {
            bind(asyncMongoRepository).to(AsyncMongoRepository.class).in(Singleton.class);
        }
    }

    private void bindRedisRepository() {
        if (redisRepository == null) {
            String redisType = System.getProperty("redis.type");
            LOG.debug("Redis type: {}", redisType);
            if (Constants.POOL_REDIS_TYPE.getValue().equalsIgnoreCase(redisType)) {
                bind(RedisPoolRepositoryImpl.class).to(RedisRepository.class).in(Singleton.class);
            } else if (Constants.CLUSTER_REDIS_TYPE.getValue().equalsIgnoreCase(redisType)) {
                bind(RedisClusterRepositoryImpl.class).to(RedisRepository.class).in(Singleton.class);
            } else {
                throw new RedisBinderRuntimeException(ErrorType.REDIS_NOT_EXISTING_TYPE_ERROR);
            }
        } else {
            bind(redisRepository).to(RedisRepository.class).in(Singleton.class);
        }
    }

    private void bindCryptoService() {
        if (cryptoService == null) {
            bind(CryptoServiceImpl.class).to(CryptoService.class).in(Singleton.class);
        } else {
            bind(cryptoService).to(CryptoService.class).in(Singleton.class);
        }
    }

    private void bindKeyBuilder() {
        if (keyBuilder == null) {
            bind(KeyBuilderImpl.class).to(KeyBuilder.class).in(Singleton.class);
        } else {
            bind(keyBuilder).to(KeyBuilder.class).in(Singleton.class);
        }
    }

    public static class ApplicationBinderBuilder {

        private ConverterService converterService;
        private GenerationService generationService;
        private ErrorResponseBuilderService errorResponseBuilderService;
        private PrometheusMeterRegistry prometheusMeterRegistry;
        private DatabaseConnectorService databaseConnectorService;
        private com.mongodb.client.MongoDatabase syncMongoDatabase;
        private com.mongodb.async.client.MongoDatabase asyncMongoDatabase;
        private JedisPool jedisPool;
        private JedisCluster jedisCluster;
        private SyncMongoRepository syncMongoRepository;
        private AsyncMongoRepository asyncMongoRepository;
        private RedisRepository redisRepository;
        private CryptoService cryptoService;
        private KeyBuilder keyBuilder;

        public ApplicationBinderBuilder setConverterService(ConverterService converterService) {
            this.converterService = converterService;
            return this;
        }

        public ApplicationBinderBuilder setGenerationService(GenerationService generationService) {
            this.generationService = generationService;
            return this;
        }

        public ApplicationBinderBuilder setErrorResponseBuilderService(ErrorResponseBuilderService errorResponseBuilderService) {
            this.errorResponseBuilderService = errorResponseBuilderService;
            return this;
        }

        public ApplicationBinderBuilder setPrometheusMeterRegistry(PrometheusMeterRegistry prometheusMeterRegistry) {
            this.prometheusMeterRegistry = prometheusMeterRegistry;
            return this;
        }

        public ApplicationBinderBuilder setDatabaseConnectorService(DatabaseConnectorService databaseConnectorService) {
            this.databaseConnectorService = databaseConnectorService;
            return this;
        }

        public ApplicationBinderBuilder setSyncMongoDatabase(com.mongodb.client.MongoDatabase syncMongoDatabase) {
            this.syncMongoDatabase = syncMongoDatabase;
            return this;
        }

        public ApplicationBinderBuilder setAsyncMongoDatabase(com.mongodb.async.client.MongoDatabase asyncMongoDatabase) {
            this.asyncMongoDatabase = asyncMongoDatabase;
            return this;
        }

        public ApplicationBinderBuilder setJedisPool(JedisPool jedisPool) {
            this.jedisPool = jedisPool;
            return this;
        }

        public ApplicationBinderBuilder setJedisCluster(JedisCluster jedisCluster) {
            this.jedisCluster = jedisCluster;
            return this;
        }

        public ApplicationBinderBuilder setSyncMongoRepository(SyncMongoRepository syncMongoRepository) {
            this.syncMongoRepository = syncMongoRepository;
            return this;
        }

        public ApplicationBinderBuilder setAsyncMongoRepository(AsyncMongoRepository asyncMongoRepository) {
            this.asyncMongoRepository = asyncMongoRepository;
            return this;
        }

        public ApplicationBinderBuilder setRedisRepository(RedisRepository redisRepository) {
            this.redisRepository = redisRepository;
            return this;
        }

        public ApplicationBinderBuilder setCryptoService(CryptoService cryptoService) {
            this.cryptoService = cryptoService;
            return this;
        }

        public ApplicationBinderBuilder setKeyBuilder(KeyBuilder keyBuilder) {
            this.keyBuilder = keyBuilder;
            return this;
        }

        public ApplicationBinder build() {
            return new ApplicationBinder(converterService, generationService, errorResponseBuilderService, prometheusMeterRegistry, databaseConnectorService, syncMongoDatabase, asyncMongoDatabase, jedisPool, jedisCluster, syncMongoRepository, asyncMongoRepository, redisRepository, cryptoService, keyBuilder);
        }
    }
}
