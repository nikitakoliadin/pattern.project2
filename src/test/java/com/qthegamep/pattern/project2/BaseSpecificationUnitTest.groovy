package com.qthegamep.pattern.project2

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.mongodb.client.MongoDatabase
import com.qthegamep.pattern.project2.binder.application.ApplicationBinder
import com.qthegamep.pattern.project2.binder.property.PropertyBinder
import com.qthegamep.pattern.project2.binder.property.PropertyInjectionResolver
import com.qthegamep.pattern.project2.config.ApplicationConfig
import com.qthegamep.pattern.project2.exception.mapper.GeneralExceptionMapper
import com.qthegamep.pattern.project2.repository.mongo.MongoRepositoryAsync
import com.qthegamep.pattern.project2.repository.mongo.MongoRepositorySync
import com.qthegamep.pattern.project2.repository.redis.RedisRepository
import com.qthegamep.pattern.project2.service.ConverterService
import com.qthegamep.pattern.project2.service.ExitManagerService
import com.qthegamep.pattern.project2.service.HashService
import com.qthegamep.pattern.project2.service.DatabaseConnectorService
import com.qthegamep.pattern.project2.service.ErrorResponseBuilderService
import com.qthegamep.pattern.project2.service.GenerationService
import com.qthegamep.pattern.project2.service.IOStrategyFactoryService
import com.qthegamep.pattern.project2.service.KeyBuilderService
import com.qthegamep.pattern.project2.service.BeanValidationService
import com.qthegamep.pattern.project2.service.LocaleService
import com.qthegamep.pattern.project2.statistics.Meters
import de.flapdoodle.embed.mongo.Command
import de.flapdoodle.embed.mongo.MongodProcess
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.DownloadConfigBuilder
import de.flapdoodle.embed.mongo.config.ExtractedArtifactStoreBuilder
import de.flapdoodle.embed.mongo.config.IMongodConfig
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.config.IRuntimeConfig
import de.flapdoodle.embed.process.config.store.HttpProxyFactory
import de.flapdoodle.embed.process.runtime.Network
import io.micrometer.core.instrument.Clock
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.prometheus.client.CollectorRegistry
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration
import org.aspectj.util.FileUtil
import org.bson.codecs.configuration.CodecRegistry
import org.glassfish.jersey.internal.inject.InjectionManager
import org.glassfish.jersey.internal.inject.Injections
import redis.clients.jedis.JedisCluster
import redis.clients.jedis.JedisPool
import redis.embedded.RedisServer
import spock.lang.Specification

import javax.validation.Validator
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

class BaseSpecificationUnitTest extends Specification {

    protected ApplicationConfig applicationConfig
    protected PropertyBinder.Builder propertyBinderBuilder
    protected PropertyBinder propertyBinder
    protected ApplicationBinder.Builder applicationBinderBuilder
    protected ApplicationBinder applicationBinder
    protected InjectionManager injectionManager

    private MongodProcess mongoServer
    private RedisServer redisServer

    void setup() {
        setupApplicationConfig()
        setupPropertyBinder()
        setupApplicationBinder()
        setupInjectionManager()
        setupMeters()
    }

    def setupApplicationConfig() {
        System.setProperty("config.properties", getAbsolutePath("src/test/resources/config-test.properties"))
        applicationConfig = new ApplicationConfig()
        applicationConfig.init()
    }

    def setupPropertyBinder() {
        PropertyInjectionResolver propertyInjectionResolverMock = new PropertyInjectionResolver()
        propertyBinderBuilder = PropertyBinder.builder()
                .setPropertyInjectionResolver(propertyInjectionResolverMock)
        propertyBinder = propertyBinderBuilder.build()
    }

    def setupApplicationBinder() {
        Runtime runtimeMock = Mock()
        ExitManagerService exitManagerServiceMock = Mock()
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
        MongoRepositorySync mongoRepositorySyncMock = Mock()
        MongoRepositoryAsync mongoRepositoryAsyncMock = Mock()
        RedisRepository redisRepositoryMock = Mock()
        HashService hashServiceMock = Mock()
        KeyBuilderService keyBuilderServiceMock = Mock()
        GeneralExceptionMapper generalExceptionMapperMock = Mock()
        BeanValidationService beanValidationServiceMock = Mock()
        IOStrategyFactoryService ioStrategyFactoryServiceMock = Mock()
        LocaleService localeServiceMock = Mock()
        applicationBinderBuilder = ApplicationBinder.builder()
                .setRuntime(runtimeMock)
                .setExitManagerService(exitManagerServiceMock)
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
                .setMongoRepositorySync(mongoRepositorySyncMock)
                .setMongoRepositoryAsync(mongoRepositoryAsyncMock)
                .setRedisRepository(redisRepositoryMock)
                .setHashService(hashServiceMock)
                .setKeyBuilderService(keyBuilderServiceMock)
                .setGeneralExceptionMapper(generalExceptionMapperMock)
                .setBeanValidationService(beanValidationServiceMock)
                .setIoStrategyFactoryService(ioStrategyFactoryServiceMock)
                .setLocaleService(localeServiceMock)
        applicationBinder = applicationBinderBuilder.build()
    }

    def setupInjectionManager() {
        injectionManager = Injections.createInjectionManager()
        injectionManager.register(propertyBinder)
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

    def getAbsolutePath(String path) {
        return Paths.get(path)
                .toAbsolutePath()
                .toString()
    }

    def createDirectory(String path) {
        File directory = new File(path)
        directory.mkdirs()
    }

    def createFile(String path, String data) {
        File file = new File(path)
        FileUtil.writeAsString(file, data)
    }

    def createCertificate(String path, String data) {
        Base64.Encoder encoder = Base64.getEncoder()
        String base64Data = encoder.encodeToString(data.getBytes())
        createFile(path, base64Data)
    }

    def readFromFile(String path) {
        return Files.lines(Paths.get(path), StandardCharsets.UTF_8)
                .collect(Collectors.toList())
                .join("")
    }

    def deleteDirectory(String path) {
        File directory = new File(path)
        deleteDirectory(directory)
    }

    def deleteDirectory(File directory) {
        File[] files = directory.listFiles()
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file)
                } else {
                    file.delete()
                }
            }
        }
        return directory.delete()
    }

    def deleteFile(String path) {
        File file = new File(path)
        FileUtil.deleteContents(file)
    }

    def startEmbeddedMongo(String host, Integer port) {
        IRuntimeConfig runtimeConfig = new RuntimeConfigBuilder()
                .defaults(Command.MongoD)
                .artifactStore(new ExtractedArtifactStoreBuilder()
                        .defaults(Command.MongoD)
                        .download(new DownloadConfigBuilder()
                                .defaultsForCommand(Command.MongoD)
                                .proxyFactory(new HttpProxyFactory("test.proxy", 8080))))
                .build()
        IMongodConfig mongoConfig = new MongodConfigBuilder()
                .version(Version.Main.V3_2)
                .net(new Net(host, port, Network.localhostIsIPv6()))
                .build()
        mongoServer = MongodStarter.getInstance(runtimeConfig)
                .prepare(mongoConfig)
                .start()
    }

    def stopEmbeddedMongo() {
        if (mongoServer != null) {
            mongoServer.stop()
        }
    }

    def startEmbeddedRedis(Integer port) {
        redisServer = new RedisServer(port)
        redisServer.start()
    }

    def stopEmbeddedRedis() {
        if (redisServer != null) {
            redisServer.stop()
        }
    }

    def "Should create objects for tests"() {
        expect: "not empty objects for tests"
        applicationConfig != null
        propertyBinderBuilder != null
        propertyBinder != null
        applicationBinderBuilder != null
        applicationBinder != null
        injectionManager != null
        and: "correct application config"
        !System.getProperty("system.type").isEmpty()
        !applicationConfig.getProperties().isEmpty()
        and: "correct property binder"
        propertyBinder.getPropertyInjectionResolver() != null
        and: "correct application binder"
        applicationBinder.getRuntime() != null
        applicationBinder.getExitManagerService() != null
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
        applicationBinder.getMongoRepositorySync() != null
        applicationBinder.getMongoRepositoryAsync() != null
        applicationBinder.getRedisRepository() != null
        applicationBinder.getHashService() != null
        applicationBinder.getKeyBuilderService() != null
        applicationBinder.getGeneralExceptionMapper() != null
        applicationBinder.getBeanValidationService() != null
        applicationBinder.getIoStrategyFactoryService() != null
        applicationBinder.getLocaleService() != null
        and: "correct injection manager"
        getInstance(propertyBinder.getPropertyInjectionResolver().getClass()) != null
        getInstance(applicationBinder.getRuntime().getClass()) != null
        getInstance(applicationBinder.getExitManagerService().getClass()) != null
        getInstance(applicationBinder.getOpenAPIConfiguration().getClass()) != null
        getInstance(applicationBinder.getObjectMapper().getClass()) != null
        getInstance(applicationBinder.getXmlMapper().getClass()) != null
        getInstance(applicationBinder.getValidator().getClass()) != null
        getInstance(applicationBinder.getConverterService().getClass()) != null
        getInstance(applicationBinder.getGenerationService().getClass()) != null
        getInstance(applicationBinder.getErrorResponseBuilderService().getClass()) != null
        getInstance(applicationBinder.getPrometheusMeterRegistry().getClass()) != null
        getInstance(applicationBinder.getCodecRegistry().getClass()) != null
        getInstance(applicationBinder.getDatabaseConnectorService().getClass()) != null
        getInstance(applicationBinder.getSyncMongoDatabase().getClass()) != null
        getInstance(applicationBinder.getAsyncMongoDatabase().getClass()) != null
        getInstance(applicationBinder.getJedisPool().getClass()) != null
        getInstance(applicationBinder.getJedisCluster().getClass()) != null
        getInstance(applicationBinder.getMongoRepositorySync().getClass()) != null
        getInstance(applicationBinder.getMongoRepositoryAsync().getClass()) != null
        getInstance(applicationBinder.getRedisRepository().getClass()) != null
        getInstance(applicationBinder.getHashService().getClass()) != null
        getInstance(applicationBinder.getKeyBuilderService().getClass()) != null
        getInstance(applicationBinder.getGeneralExceptionMapper().getClass()) != null
        getInstance(applicationBinder.getBeanValidationService().getClass()) != null
        getInstance(applicationBinder.getIoStrategyFactoryService().getClass()) != null
        getInstance(applicationBinder.getLocaleService().getClass()) != null
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
