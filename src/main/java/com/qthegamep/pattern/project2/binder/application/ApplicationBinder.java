package com.qthegamep.pattern.project2.binder.application;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mongodb.MongoClient;
import com.qthegamep.pattern.project2.exception.runtime.RedisRepositoryApplicationBinderInitializationRuntimeException;
import com.qthegamep.pattern.project2.exception.mapper.GeneralExceptionMapper;
import com.qthegamep.pattern.project2.model.container.MongoConnection;
import com.qthegamep.pattern.project2.model.container.RedisConnection;
import com.qthegamep.pattern.project2.service.adapter.IsoDateJsonModuleAdapter;
import com.qthegamep.pattern.project2.service.adapter.NullJsonConventionAdapter;
import com.qthegamep.pattern.project2.service.adapter.ObjectIdJsonModuleAdapter;
import com.qthegamep.pattern.project2.statistics.meter.*;
import com.qthegamep.pattern.project2.repository.mongo.MongoRepositoryAsync;
import com.qthegamep.pattern.project2.repository.mongo.MongoRepositoryAsyncImpl;
import com.qthegamep.pattern.project2.repository.mongo.MongoRepositorySync;
import com.qthegamep.pattern.project2.repository.mongo.MongoRepositorySyncImpl;
import com.qthegamep.pattern.project2.repository.redis.RedisRepositoryClusterImpl;
import com.qthegamep.pattern.project2.repository.redis.RedisRepositoryPoolImpl;
import com.qthegamep.pattern.project2.repository.redis.RedisRepository;
import com.qthegamep.pattern.project2.service.*;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.binder.jvm.*;
import io.micrometer.core.instrument.binder.mongodb.MongoMetricsCommandListener;
import io.micrometer.core.instrument.binder.mongodb.MongoMetricsConnectionPoolListener;
import io.micrometer.core.instrument.binder.system.*;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import javax.inject.Singleton;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;

public class ApplicationBinder extends AbstractBinder {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationBinder.class);

    private Runtime runtime;
    private ExitManagerService exitManagerService;
    private OpenAPIConfiguration openAPIConfiguration;
    private ObjectMapper objectMapper;
    private XmlMapper xmlMapper;
    private Validator validator;
    private ConverterService converterService;
    private GenerationService generationService;
    private ErrorResponseBuilderService errorResponseBuilderService;
    private PrometheusMeterRegistry prometheusMeterRegistry;
    private CodecRegistry codecRegistry;
    private DatabaseConnectorService databaseConnectorService;
    private com.mongodb.client.MongoDatabase syncMongoDatabase;
    private com.mongodb.async.client.MongoDatabase asyncMongoDatabase;
    private JedisPool jedisPool;
    private JedisCluster jedisCluster;
    private MongoRepositorySync mongoRepositorySync;
    private MongoRepositoryAsync mongoRepositoryAsync;
    private RedisRepository redisRepository;
    private HashService hashService;
    private KeyBuilderService keyBuilderService;
    private GeneralExceptionMapper generalExceptionMapper;
    private BeanValidationService beanValidationService;
    private IOStrategyFactoryService ioStrategyFactoryService;

    private MongoMetricsCommandListener mongoMetricsCommandListener;
    private MongoMetricsConnectionPoolListener mongoMetricsConnectionPoolListener;

    private ApplicationBinder(Runtime runtime,
                              ExitManagerService exitManagerService,
                              OpenAPIConfiguration openAPIConfiguration,
                              ObjectMapper objectMapper,
                              XmlMapper xmlMapper,
                              Validator validator,
                              ConverterService converterService,
                              GenerationService generationService,
                              ErrorResponseBuilderService errorResponseBuilderService,
                              PrometheusMeterRegistry prometheusMeterRegistry,
                              CodecRegistry codecRegistry,
                              DatabaseConnectorService databaseConnectorService,
                              com.mongodb.client.MongoDatabase syncMongoDatabase,
                              com.mongodb.async.client.MongoDatabase asyncMongoDatabase,
                              JedisPool jedisPool,
                              JedisCluster jedisCluster,
                              MongoRepositorySync mongoRepositorySync,
                              MongoRepositoryAsync mongoRepositoryAsync,
                              RedisRepository redisRepository,
                              HashService hashService,
                              KeyBuilderService keyBuilderService,
                              GeneralExceptionMapper generalExceptionMapper,
                              BeanValidationService beanValidationService,
                              IOStrategyFactoryService ioStrategyFactoryService) {
        this.runtime = runtime;
        this.exitManagerService = exitManagerService;
        this.openAPIConfiguration = openAPIConfiguration;
        this.objectMapper = objectMapper;
        this.xmlMapper = xmlMapper;
        this.validator = validator;
        this.converterService = converterService;
        this.generationService = generationService;
        this.errorResponseBuilderService = errorResponseBuilderService;
        this.prometheusMeterRegistry = prometheusMeterRegistry;
        this.codecRegistry = codecRegistry;
        this.databaseConnectorService = databaseConnectorService;
        this.syncMongoDatabase = syncMongoDatabase;
        this.asyncMongoDatabase = asyncMongoDatabase;
        this.jedisPool = jedisPool;
        this.jedisCluster = jedisCluster;
        this.mongoRepositorySync = mongoRepositorySync;
        this.mongoRepositoryAsync = mongoRepositoryAsync;
        this.redisRepository = redisRepository;
        this.hashService = hashService;
        this.keyBuilderService = keyBuilderService;
        this.generalExceptionMapper = generalExceptionMapper;
        this.beanValidationService = beanValidationService;
        this.ioStrategyFactoryService = ioStrategyFactoryService;
    }

    public Runtime getRuntime() {
        return runtime;
    }

    public ExitManagerService getExitManagerService() {
        return exitManagerService;
    }

    public OpenAPIConfiguration getOpenAPIConfiguration() {
        return openAPIConfiguration;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public XmlMapper getXmlMapper() {
        return xmlMapper;
    }

    public Validator getValidator() {
        return validator;
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

    public CodecRegistry getCodecRegistry() {
        return codecRegistry;
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

    public MongoRepositorySync getMongoRepositorySync() {
        return mongoRepositorySync;
    }

    public MongoRepositoryAsync getMongoRepositoryAsync() {
        return mongoRepositoryAsync;
    }

    public RedisRepository getRedisRepository() {
        return redisRepository;
    }

    public HashService getHashService() {
        return hashService;
    }

    public KeyBuilderService getKeyBuilderService() {
        return keyBuilderService;
    }

    public GeneralExceptionMapper getGeneralExceptionMapper() {
        return generalExceptionMapper;
    }

    public BeanValidationService getBeanValidationService() {
        return beanValidationService;
    }

    public IOStrategyFactoryService getIoStrategyFactoryService() {
        return ioStrategyFactoryService;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected void configure() {
        bindRuntime();
        bindExitManagerService();
        bindOpenAPIConfiguration();
        bindObjectMapper();
        bindXmlMapper();
        bindValidator();
        bindConverterService();
        bindGenerationService();
        bindErrorResponseBuilderService();
        bindPrometheusMeterRegistry();
        bindCodecRegistry();
        bindDatabaseConnector();
        bindSyncMongoDatabase();
        bindAsyncMongoDatabase();
        bindJedisPool();
        bindJedisCluster();
        bindMongoRepositorySync();
        bindMongoRepositoryAsync();
        bindRedisRepository();
        bindHashService();
        bindKeyBuilder();
        bindGeneralExceptionMapper();
        bindBeanValidationService();
        bindIOStrategyFactoryService();
    }

    private void bindRuntime() {
        if (runtime == null) {
            runtime = Runtime.getRuntime();
        }
        bind(runtime).to(Runtime.class).in(Singleton.class);
    }

    private void bindExitManagerService() {
        if (exitManagerService == null) {
            exitManagerService = new ExitManagerServiceImpl(runtime);
        }
        bind(exitManagerService).to(ExitManagerService.class).in(Singleton.class);
    }

    private void bindOpenAPIConfiguration() {
        if (openAPIConfiguration == null) {
            openAPIConfiguration = new SwaggerConfiguration()
                    .openAPI(new OpenAPI())
                    .prettyPrint(true);
        }
        bind(openAPIConfiguration).to(OpenAPIConfiguration.class).in(Singleton.class);
    }

    private void bindObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper()
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
                    .registerModule(new IsoDateJsonModuleAdapter().buildModule())
                    .registerModule(new ObjectIdJsonModuleAdapter().buildModule());
        }
        bind(objectMapper).to(ObjectMapper.class).in(Singleton.class);
    }

    private void bindXmlMapper() {
        if (xmlMapper == null) {
            bindAsContract(XmlMapper.class).to(Singleton.class);
        } else {
            bind(xmlMapper).to(XmlMapper.class).in(Singleton.class);
        }
    }

    private void bindValidator() {
        if (validator == null) {
            ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
            validator = validatorFactory.getValidator();
        }
        bind(validator).to(Validator.class).in(Singleton.class);
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
            new TaskQueueSizeMeter().bindTo(newPrometheusMeterRegistry);
            new GrizzlyThreadPoolMeter().bindTo(newPrometheusMeterRegistry);
            new ErrorTypesMeter().bindTo(newPrometheusMeterRegistry);
            new ResponseStatusMeter().bindTo(newPrometheusMeterRegistry);
            new RequestCounterMeter().bindTo(newPrometheusMeterRegistry);
            new RequestTimeMeter().bindTo(newPrometheusMeterRegistry);
            new RedisErrorCounterMeter().bindTo(newPrometheusMeterRegistry);
            mongoMetricsCommandListener = new MongoMetricsCommandListener(newPrometheusMeterRegistry);
            mongoMetricsConnectionPoolListener = new MongoMetricsConnectionPoolListener(newPrometheusMeterRegistry);
            bind(newPrometheusMeterRegistry).to(PrometheusMeterRegistry.class).in(Singleton.class);
        } else {
            bind(prometheusMeterRegistry).to(PrometheusMeterRegistry.class).in(Singleton.class);
        }
    }

    private void bindCodecRegistry() {
        if (codecRegistry == null) {
            codecRegistry = CodecRegistries.fromRegistries(
                    MongoClient.getDefaultCodecRegistry(),
                    CodecRegistries.fromProviders(
                            PojoCodecProvider.builder()
                                    .conventions(Arrays.asList(
                                            Conventions.CLASS_AND_PROPERTY_CONVENTION,
                                            Conventions.ANNOTATION_CONVENTION,
                                            new NullJsonConventionAdapter()))
                                    .automatic(true)
                                    .build()));
        }
        bind(codecRegistry).to(CodecRegistry.class).in(Singleton.class);
    }

    private void bindDatabaseConnector() {
        if (databaseConnectorService == null) {
            String syncMongoDbStandaloneHost = System.getProperty("sync.mongodb.standalone.host");
            String syncMongoDbStandalonePort = System.getProperty("sync.mongodb.standalone.port");
            String syncMongoDbStandaloneUser = System.getProperty("sync.mongodb.standalone.user");
            String syncMongoDbStandaloneDb = System.getProperty("sync.mongodb.standalone.db");
            String syncMongoDbStandalonePassword = System.getProperty("sync.mongodb.standalone.pass");
            String syncMongoDbClusterPort = System.getProperty("sync.mongodb.cluster.port");
            String syncMongoDbClusterUser = System.getProperty("sync.mongodb.cluster.user");
            String syncMongoDbClusterDb = System.getProperty("sync.mongodb.cluster.db");
            String syncMongoDbClusterPassword = System.getProperty("sync.mongodb.cluster.pass");
            String asyncMongoDbStandaloneHost = System.getProperty("async.mongodb.standalone.host");
            String asyncMongoDbStandalonePort = System.getProperty("async.mongodb.standalone.port");
            String asyncMongoDbStandaloneUser = System.getProperty("async.mongodb.standalone.user");
            String asyncMongoDbStandaloneDb = System.getProperty("async.mongodb.standalone.db");
            String asyncMongoDbStandalonePassword = System.getProperty("async.mongodb.standalone.pass");
            String asyncMongoDbClusterPort = System.getProperty("async.mongodb.cluster.port");
            String asyncMongoDbClusterUser = System.getProperty("async.mongodb.cluster.user");
            String asyncMongoDbClusterDb = System.getProperty("async.mongodb.cluster.db");
            String asyncMongoDbClusterPassword = System.getProperty("async.mongodb.cluster.pass");
            String redisPoolHost = System.getProperty("redis.pool.host");
            String redisPoolPort = System.getProperty("redis.pool.port");
            String redisPoolPassword = System.getProperty("redis.pool.password");
            String redisPoolMaxTotal = System.getProperty("redis.pool.max.total");
            String redisPoolMaxIdle = System.getProperty("redis.pool.max.idle");
            String redisPoolTimeout = System.getProperty("redis.pool.timeout");
            String redisClusterPassword = System.getProperty("redis.cluster.password");
            String redisClusterTestOnBorrow = System.getProperty("redis.cluster.test.on.borrow");
            String redisClusterTestOnReturn = System.getProperty("redis.cluster.test.on.return");
            String redisClusterMaxTotal = System.getProperty("redis.cluster.max.total");
            String redisClusterMaxIdle = System.getProperty("redis.cluster.max.idle");
            String redisClusterMinIdle = System.getProperty("redis.cluster.min.idle");
            String redisClusterConnectionTimeout = System.getProperty("redis.cluster.connection.timeout");
            String redisClusterSoTimeout = System.getProperty("redis.cluster.so.timeout");
            String redisClusterMaxAttempts = System.getProperty("redis.cluster.max.attempts");
            databaseConnectorService = new DatabaseConnectorServiceImpl(
                    syncMongoDbStandaloneHost,
                    syncMongoDbStandalonePort,
                    syncMongoDbStandaloneUser,
                    syncMongoDbStandaloneDb,
                    syncMongoDbStandalonePassword,
                    syncMongoDbClusterPort,
                    syncMongoDbClusterUser,
                    syncMongoDbClusterDb,
                    syncMongoDbClusterPassword,
                    asyncMongoDbStandaloneHost,
                    asyncMongoDbStandalonePort,
                    asyncMongoDbStandaloneUser,
                    asyncMongoDbStandaloneDb,
                    asyncMongoDbStandalonePassword,
                    asyncMongoDbClusterPort,
                    asyncMongoDbClusterUser,
                    asyncMongoDbClusterDb,
                    asyncMongoDbClusterPassword,
                    redisPoolHost,
                    redisPoolPort,
                    redisPoolPassword,
                    redisPoolMaxTotal,
                    redisPoolMaxIdle,
                    redisPoolTimeout,
                    redisClusterPassword,
                    redisClusterTestOnBorrow,
                    redisClusterTestOnReturn,
                    redisClusterMaxTotal,
                    redisClusterMaxIdle,
                    redisClusterMinIdle,
                    redisClusterConnectionTimeout,
                    redisClusterSoTimeout,
                    redisClusterMaxAttempts,
                    exitManagerService);
        }
        bind(databaseConnectorService).to(DatabaseConnectorService.class).in(Singleton.class);
    }

    private void bindSyncMongoDatabase() {
        if (syncMongoDatabase == null) {
            String syncMongoDbEnabled = System.getProperty("sync.mongodb.enabled");
            LOG.debug("Sync MongoDB enabled: {}", syncMongoDbEnabled);
            if (Boolean.parseBoolean(syncMongoDbEnabled)) {
                String syncMongoDbType = System.getProperty("sync.mongodb.type");
                LOG.info("Sync MongoDB type: {}", syncMongoDbType);
                com.mongodb.client.MongoDatabase newSyncMongoDatabase = databaseConnectorService.connectToSyncMongoDB(mongoMetricsCommandListener, mongoMetricsConnectionPoolListener, codecRegistry, MongoConnection.valueOf(syncMongoDbType));
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
                com.mongodb.async.client.MongoDatabase newAsyncMongoDatabase = databaseConnectorService.connectToAsyncMongoDB(mongoMetricsCommandListener, mongoMetricsConnectionPoolListener, codecRegistry, MongoConnection.valueOf(asyncMongoDbType));
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

    private void bindMongoRepositorySync() {
        if (mongoRepositorySync == null) {
            bind(MongoRepositorySyncImpl.class).to(MongoRepositorySync.class).in(Singleton.class);
        } else {
            bind(mongoRepositorySync).to(MongoRepositorySync.class).in(Singleton.class);
        }
    }

    private void bindMongoRepositoryAsync() {
        if (mongoRepositoryAsync == null) {
            bind(MongoRepositoryAsyncImpl.class).to(MongoRepositoryAsync.class).in(Singleton.class);
        } else {
            bind(mongoRepositoryAsync).to(MongoRepositoryAsync.class).in(Singleton.class);
        }
    }

    private void bindRedisRepository() {
        if (redisRepository == null) {
            String redisType = System.getProperty("redis.type");
            LOG.debug("Redis type: {}", redisType);
            if (RedisConnection.POOL_REDIS_CONNECTION.getType().equalsIgnoreCase(redisType) || RedisConnection.MASTER_SLAVE_REDIS_CONNECTION.getType().equalsIgnoreCase(redisType)) {
                bind(RedisRepositoryPoolImpl.class).to(RedisRepository.class).in(Singleton.class);
            } else if (RedisConnection.CLUSTER_REDIS_CONNECTION.getType().equalsIgnoreCase(redisType)) {
                bind(RedisRepositoryClusterImpl.class).to(RedisRepository.class).in(Singleton.class);
            } else {
                throw new RedisRepositoryApplicationBinderInitializationRuntimeException("Not existing redis type!");
            }
        } else {
            bind(redisRepository).to(RedisRepository.class).in(Singleton.class);
        }
    }

    private void bindHashService() {
        if (hashService == null) {
            bind(HashServiceImpl.class).to(HashService.class).in(Singleton.class);
        } else {
            bind(hashService).to(HashService.class).in(Singleton.class);
        }
    }

    private void bindKeyBuilder() {
        if (keyBuilderService == null) {
            bind(KeyBuilderServiceImpl.class).to(KeyBuilderService.class).in(Singleton.class);
        } else {
            bind(keyBuilderService).to(KeyBuilderService.class).in(Singleton.class);
        }
    }

    private void bindGeneralExceptionMapper() {
        if (generalExceptionMapper == null) {
            bindAsContract(GeneralExceptionMapper.class).in(Singleton.class);
        } else {
            bind(generalExceptionMapper).to(GeneralExceptionMapper.class).in(Singleton.class);
        }
    }

    private void bindBeanValidationService() {
        if (beanValidationService == null) {
            bind(BeanValidationServiceImpl.class).to(BeanValidationService.class).in(Singleton.class);
        } else {
            bind(beanValidationService).to(BeanValidationService.class).in(Singleton.class);
        }
    }

    private void bindIOStrategyFactoryService() {
        if (ioStrategyFactoryService == null) {
            bind(IOStrategyFactoryServiceImpl.class).to(IOStrategyFactoryService.class).in(Singleton.class);
        } else {
            bind(ioStrategyFactoryService).to(IOStrategyFactoryService.class).in(Singleton.class);
        }
    }

    public static class Builder {

        private Runtime runtime;
        private ExitManagerService exitManagerService;
        private OpenAPIConfiguration openAPIConfiguration;
        private ObjectMapper objectMapper;
        private XmlMapper xmlMapper;
        private Validator validator;
        private ConverterService converterService;
        private GenerationService generationService;
        private ErrorResponseBuilderService errorResponseBuilderService;
        private PrometheusMeterRegistry prometheusMeterRegistry;
        private CodecRegistry codecRegistry;
        private DatabaseConnectorService databaseConnectorService;
        private com.mongodb.client.MongoDatabase syncMongoDatabase;
        private com.mongodb.async.client.MongoDatabase asyncMongoDatabase;
        private JedisPool jedisPool;
        private JedisCluster jedisCluster;
        private MongoRepositorySync mongoRepositorySync;
        private MongoRepositoryAsync mongoRepositoryAsync;
        private RedisRepository redisRepository;
        private HashService hashService;
        private KeyBuilderService keyBuilderService;
        private GeneralExceptionMapper generalExceptionMapper;
        private BeanValidationService beanValidationService;
        private IOStrategyFactoryService ioStrategyFactoryService;

        public Builder setRuntime(Runtime runtime) {
            this.runtime = runtime;
            return this;
        }

        public Builder setExitManagerService(ExitManagerService exitManagerService) {
            this.exitManagerService = exitManagerService;
            return this;
        }

        public Builder setOpenAPIConfiguration(OpenAPIConfiguration openAPIConfiguration) {
            this.openAPIConfiguration = openAPIConfiguration;
            return this;
        }

        public Builder setObjectMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            return this;
        }

        public Builder setXmlMapper(XmlMapper xmlMapper) {
            this.xmlMapper = xmlMapper;
            return this;
        }

        public Builder setValidator(Validator validator) {
            this.validator = validator;
            return this;
        }

        public Builder setConverterService(ConverterService converterService) {
            this.converterService = converterService;
            return this;
        }

        public Builder setGenerationService(GenerationService generationService) {
            this.generationService = generationService;
            return this;
        }

        public Builder setErrorResponseBuilderService(ErrorResponseBuilderService errorResponseBuilderService) {
            this.errorResponseBuilderService = errorResponseBuilderService;
            return this;
        }

        public Builder setPrometheusMeterRegistry(PrometheusMeterRegistry prometheusMeterRegistry) {
            this.prometheusMeterRegistry = prometheusMeterRegistry;
            return this;
        }

        public Builder setCodecRegistry(CodecRegistry codecRegistry) {
            this.codecRegistry = codecRegistry;
            return this;
        }

        public Builder setDatabaseConnectorService(DatabaseConnectorService databaseConnectorService) {
            this.databaseConnectorService = databaseConnectorService;
            return this;
        }

        public Builder setSyncMongoDatabase(com.mongodb.client.MongoDatabase syncMongoDatabase) {
            this.syncMongoDatabase = syncMongoDatabase;
            return this;
        }

        public Builder setAsyncMongoDatabase(com.mongodb.async.client.MongoDatabase asyncMongoDatabase) {
            this.asyncMongoDatabase = asyncMongoDatabase;
            return this;
        }

        public Builder setJedisPool(JedisPool jedisPool) {
            this.jedisPool = jedisPool;
            return this;
        }

        public Builder setJedisCluster(JedisCluster jedisCluster) {
            this.jedisCluster = jedisCluster;
            return this;
        }

        public Builder setMongoRepositorySync(MongoRepositorySync mongoRepositorySync) {
            this.mongoRepositorySync = mongoRepositorySync;
            return this;
        }

        public Builder setMongoRepositoryAsync(MongoRepositoryAsync mongoRepositoryAsync) {
            this.mongoRepositoryAsync = mongoRepositoryAsync;
            return this;
        }

        public Builder setRedisRepository(RedisRepository redisRepository) {
            this.redisRepository = redisRepository;
            return this;
        }

        public Builder setHashService(HashService hashService) {
            this.hashService = hashService;
            return this;
        }

        public Builder setKeyBuilderService(KeyBuilderService keyBuilderService) {
            this.keyBuilderService = keyBuilderService;
            return this;
        }

        public Builder setGeneralExceptionMapper(GeneralExceptionMapper generalExceptionMapper) {
            this.generalExceptionMapper = generalExceptionMapper;
            return this;
        }

        public Builder setBeanValidationService(BeanValidationService beanValidationService) {
            this.beanValidationService = beanValidationService;
            return this;
        }

        public Builder setIoStrategyFactoryService(IOStrategyFactoryService ioStrategyFactoryService) {
            this.ioStrategyFactoryService = ioStrategyFactoryService;
            return this;
        }

        public ApplicationBinder build() {
            return new ApplicationBinder(
                    runtime,
                    exitManagerService,
                    openAPIConfiguration,
                    objectMapper,
                    xmlMapper,
                    validator,
                    converterService,
                    generationService,
                    errorResponseBuilderService,
                    prometheusMeterRegistry,
                    codecRegistry,
                    databaseConnectorService,
                    syncMongoDatabase,
                    asyncMongoDatabase,
                    jedisPool,
                    jedisCluster,
                    mongoRepositorySync,
                    mongoRepositoryAsync,
                    redisRepository,
                    hashService,
                    keyBuilderService,
                    generalExceptionMapper,
                    beanValidationService,
                    ioStrategyFactoryService);
        }
    }
}
