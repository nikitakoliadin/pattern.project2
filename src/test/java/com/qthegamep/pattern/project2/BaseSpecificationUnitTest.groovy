package com.qthegamep.pattern.project2

import com.mongodb.client.MongoDatabase
import com.qthegamep.pattern.project2.binder.application.ApplicationBinder
import com.qthegamep.pattern.project2.config.ApplicationConfig
import com.qthegamep.pattern.project2.exception.mapper.GeneralExceptionMapper
import com.qthegamep.pattern.project2.repository.mongo.AsyncMongoRepository
import com.qthegamep.pattern.project2.repository.mongo.SyncMongoRepository
import com.qthegamep.pattern.project2.repository.redis.RedisRepository
import com.qthegamep.pattern.project2.service.ConverterService
import com.qthegamep.pattern.project2.service.CryptoService
import com.qthegamep.pattern.project2.service.DatabaseConnectorService
import com.qthegamep.pattern.project2.service.ErrorResponseBuilderService
import com.qthegamep.pattern.project2.service.GenerationService
import com.qthegamep.pattern.project2.service.IOStrategyFactoryService
import com.qthegamep.pattern.project2.service.KeyBuilderService
import com.qthegamep.pattern.project2.service.ValidationService
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.bson.codecs.configuration.CodecRegistry
import org.glassfish.jersey.internal.inject.InjectionManager
import org.glassfish.jersey.internal.inject.Injections
import redis.clients.jedis.JedisCluster
import redis.clients.jedis.JedisPool
import spock.lang.Specification

class BaseSpecificationUnitTest extends Specification {

    protected ApplicationConfig applicationConfig
    protected ApplicationBinder applicationBinder
    protected InjectionManager injectionManager

    void setup() {
        System.setProperty("config.properties", "src/main/resources/config.properties")

        applicationConfig = new ApplicationConfig()
        applicationConfig.init()

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
        CryptoService cryptoServiceMock = Mock()
        KeyBuilderService keyBuilderServiceMock = Mock()
        GeneralExceptionMapper generalExceptionMapperMock = Mock()
        ValidationService validationServiceMock = Mock()
        IOStrategyFactoryService ioStrategyFactoryServiceMock = Mock()

        applicationBinder = ApplicationBinder.builder()
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
                .setCryptoService(cryptoServiceMock)
                .setKeyBuilderService(keyBuilderServiceMock)
                .setGeneralExceptionMapper(generalExceptionMapperMock)
                .setValidationService(validationServiceMock)
                .setIoStrategyFactoryService(ioStrategyFactoryServiceMock)
                .build()

        injectionManager = Injections.createInjectionManager()
        injectionManager.register(applicationBinder)
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
