package com.qthegamep.pattern.project2

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.mongodb.client.MongoDatabase
import com.qthegamep.pattern.project2.binder.application.ApplicationBinder
import com.qthegamep.pattern.project2.config.ApplicationConfig
import com.qthegamep.pattern.project2.exception.mapper.GeneralExceptionMapper
import com.qthegamep.pattern.project2.repository.mongo.AsyncMongoRepository
import com.qthegamep.pattern.project2.repository.mongo.SyncMongoRepository
import com.qthegamep.pattern.project2.repository.redis.RedisRepository
import com.qthegamep.pattern.project2.service.ConverterService
import com.qthegamep.pattern.project2.service.HashService
import com.qthegamep.pattern.project2.service.DatabaseConnectorService
import com.qthegamep.pattern.project2.service.ErrorResponseBuilderService
import com.qthegamep.pattern.project2.service.GenerationService
import com.qthegamep.pattern.project2.service.IOStrategyFactoryService
import com.qthegamep.pattern.project2.service.KeyBuilderService
import com.qthegamep.pattern.project2.service.ValidationService
import com.qthegamep.pattern.project2.statistics.Meters
import io.micrometer.core.instrument.Clock
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.prometheus.client.CollectorRegistry
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration
import org.bson.codecs.configuration.CodecRegistry
import org.glassfish.jersey.internal.inject.InjectionManager
import org.glassfish.jersey.internal.inject.Injections
import redis.clients.jedis.JedisCluster
import redis.clients.jedis.JedisPool
import spock.lang.Specification

import javax.validation.Validator

class BaseSpecificationUnitTest extends Specification {

    protected ApplicationConfig applicationConfig
    protected ApplicationBinder applicationBinder
    protected InjectionManager injectionManager

    void setup() {
        setupApplicationConfig()
        setupApplicationBinder()
        setupInjectionManager()
        setupMeters()
    }

    def setupApplicationConfig() {
        System.setProperty("config.properties", "src/main/resources/config.properties")
        applicationConfig = new ApplicationConfig()
        applicationConfig.init()
    }

    def setupApplicationBinder() {
        OpenAPIConfiguration openAPIConfigurationMock = Mock()
        ObjectMapper objectMapperMock = Mock()
        XmlMapper xmlMapperMock = Mock()
        Validator validatorMock = Mock()
        ConverterService converterServiceMock = Mock()
        GenerationService generationServiceMock = Mock()
        ErrorResponseBuilderService errorResponseBuilderServiceMock = Mock()
        PrometheusMeterRegistry prometheusMeterRegistryMock = Spy(new PrometheusMeterRegistry(PrometheusConfig.DEFAULT, CollectorRegistry.defaultRegistry, Clock.SYSTEM))
        CodecRegistry codecRegistryMock = Mock()
        DatabaseConnectorService databaseConnectorServiceMock = Mock()
        MongoDatabase syncMongoDatabaseMock = Mock()
        com.mongodb.async.client.MongoDatabase asyncMongoDatabaseMock = Mock()
        JedisPool jedisPoolMock = Mock()
        JedisCluster jedisClusterMock = Mock()
        SyncMongoRepository syncMongoRepositoryMock = Mock()
        AsyncMongoRepository asyncMongoRepositoryMock = Mock()
        RedisRepository redisRepositoryMock = Mock()
        HashService hashServiceMock = Mock()
        KeyBuilderService keyBuilderServiceMock = Mock()
        GeneralExceptionMapper generalExceptionMapperMock = Mock()
        ValidationService validationServiceMock = Mock()
        IOStrategyFactoryService ioStrategyFactoryServiceMock = Mock()
        applicationBinder = ApplicationBinder.builder()
                .setOpenAPIConfiguration(openAPIConfigurationMock)
                .setObjectMapper(objectMapperMock)
                .setXmlMapper(xmlMapperMock)
                .setValidator(validatorMock)
                .setConverterService(converterServiceMock)
                .setGenerationService(generationServiceMock)
                .setErrorResponseBuilderService(errorResponseBuilderServiceMock)
                .setPrometheusMeterRegistry(prometheusMeterRegistryMock)
                .setCodecRegistry(codecRegistryMock)
                .setDatabaseConnectorService(databaseConnectorServiceMock)
                .setSyncMongoDatabase(syncMongoDatabaseMock)
                .setAsyncMongoDatabase(asyncMongoDatabaseMock)
                .setJedisPool(jedisPoolMock)
                .setJedisCluster(jedisClusterMock)
                .setSyncMongoRepository(syncMongoRepositoryMock)
                .setAsyncMongoRepository(asyncMongoRepositoryMock)
                .setRedisRepository(redisRepositoryMock)
                .setHashService(hashServiceMock)
                .setKeyBuilderService(keyBuilderServiceMock)
                .setGeneralExceptionMapper(generalExceptionMapperMock)
                .setValidationService(validationServiceMock)
                .setIoStrategyFactoryService(ioStrategyFactoryServiceMock)
                .build()
    }

    def setupInjectionManager() {
        injectionManager = Injections.createInjectionManager()
        injectionManager.register(applicationBinder)
    }

    def setupMeters() {
        Meters.TASK_QUEUE_SIZE_METER.set(0L)
        Meters.AVAILABLE_GRIZZLY_THREADS_METER.set(0L)
        Meters.ERROR_TYPES_METER.forEach({ key, value -> value.set(0L) })
        Meters.RESPONSE_STATUS_METER.forEach({ key, value -> value.set(0L) })
        Meters.REQUEST_COUNTER_METER.forEach({ key, value -> value.set(0L) })
        Meters.AVERAGE_REQUEST_TIME_METER.forEach({ key, value -> value.clear() })
        Meters.MAX_REQUEST_TIME_METER.forEach({ key, value -> value.clear() })
    }

    def getInstance(Class clazz) {
        return injectionManager.getInstance(clazz)
    }

    def "Should create objects for tests"() {
        expect: "not empty objects for tests"
        applicationConfig != null
        applicationBinder != null
        injectionManager != null
        and: "correct application config"
        !System.getProperty("system.type").isEmpty()
        !applicationConfig.getProperties().isEmpty()
        and: "correct application binder"
        applicationBinder.getOpenAPIConfiguration() != null
        applicationBinder.getObjectMapper() != null
        applicationBinder.getXmlMapper() != null
        applicationBinder.getValidator() != null
        applicationBinder.getConverterService() != null
        applicationBinder.getGenerationService() != null
        applicationBinder.getErrorResponseBuilderService() != null
        applicationBinder.getPrometheusMeterRegistry() != null
        applicationBinder.getCodecRegistry() != null
        applicationBinder.getDatabaseConnectorService() != null
        applicationBinder.getSyncMongoDatabase() != null
        applicationBinder.getAsyncMongoDatabase() != null
        applicationBinder.getJedisPool() != null
        applicationBinder.getJedisCluster() != null
        applicationBinder.getSyncMongoRepository() != null
        applicationBinder.getAsyncMongoRepository() != null
        applicationBinder.getRedisRepository() != null
        applicationBinder.getHashService() != null
        applicationBinder.getKeyBuilderService() != null
        applicationBinder.getGeneralExceptionMapper() != null
        applicationBinder.getValidationService() != null
        applicationBinder.getIoStrategyFactoryService() != null
        and: "correct injection manager"
        injectionManager.getInstance(applicationBinder.getOpenAPIConfiguration().getClass()) != null
        injectionManager.getInstance(applicationBinder.getObjectMapper().getClass()) != null
        injectionManager.getInstance(applicationBinder.getXmlMapper().getClass()) != null
        injectionManager.getInstance(applicationBinder.getValidator().getClass()) != null
        injectionManager.getInstance(applicationBinder.getConverterService().getClass()) != null
        injectionManager.getInstance(applicationBinder.getGenerationService().getClass()) != null
        injectionManager.getInstance(applicationBinder.getErrorResponseBuilderService().getClass()) != null
        injectionManager.getInstance(applicationBinder.getPrometheusMeterRegistry().getClass()) != null
        injectionManager.getInstance(applicationBinder.getCodecRegistry().getClass()) != null
        injectionManager.getInstance(applicationBinder.getDatabaseConnectorService().getClass()) != null
        injectionManager.getInstance(applicationBinder.getSyncMongoDatabase().getClass()) != null
        injectionManager.getInstance(applicationBinder.getAsyncMongoDatabase().getClass()) != null
        injectionManager.getInstance(applicationBinder.getJedisPool().getClass()) != null
        injectionManager.getInstance(applicationBinder.getJedisCluster().getClass()) != null
        injectionManager.getInstance(applicationBinder.getSyncMongoRepository().getClass()) != null
        injectionManager.getInstance(applicationBinder.getAsyncMongoRepository().getClass()) != null
        injectionManager.getInstance(applicationBinder.getRedisRepository().getClass()) != null
        injectionManager.getInstance(applicationBinder.getHashService().getClass()) != null
        injectionManager.getInstance(applicationBinder.getKeyBuilderService().getClass()) != null
        injectionManager.getInstance(applicationBinder.getGeneralExceptionMapper().getClass()) != null
        injectionManager.getInstance(applicationBinder.getValidationService().getClass()) != null
        injectionManager.getInstance(applicationBinder.getIoStrategyFactoryService().getClass()) != null
        and: "correct metrics"
        Meters.TASK_QUEUE_SIZE_METER.get() == 0L
        Meters.AVAILABLE_GRIZZLY_THREADS_METER.get() == 0L
        Meters.ERROR_TYPES_METER.each { key, value -> value.get() == 0L }
        Meters.RESPONSE_STATUS_METER.each { key, value -> value.get() == 0L }
        Meters.REQUEST_COUNTER_METER.each { key, value -> value.get() == 0L }
        Meters.AVERAGE_REQUEST_TIME_METER.each { key, value -> value.isEmpty() }
        Meters.MAX_REQUEST_TIME_METER.each { key, value -> value.isEmpty() }
        Meters.REDIS_ERROR_COUNTER_METER.get() == 0L
    }
}
