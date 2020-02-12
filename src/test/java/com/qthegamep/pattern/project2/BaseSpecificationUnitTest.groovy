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
import io.micrometer.prometheus.PrometheusMeterRegistry
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
        PrometheusMeterRegistry prometheusMeterRegistryMock = Mock()
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
        expect: "correct objects for tests"
        applicationConfig != null
        applicationBinder != null
        injectionManager != null
    }
}
