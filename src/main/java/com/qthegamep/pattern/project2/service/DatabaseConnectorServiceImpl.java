package com.qthegamep.pattern.project2.service;

import com.google.common.base.Strings;
import com.mongodb.Mongo;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.async.client.MongoClients;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.event.CommandListener;
import com.mongodb.event.ConnectionPoolListener;
import com.qthegamep.pattern.project2.binder.property.Property;
import com.qthegamep.pattern.project2.model.container.MongoConnection;
import com.qthegamep.pattern.project2.repository.mongo.callback.ShutdownServerCallback;
import com.qthegamep.pattern.project2.exception.runtime.AsyncMongoDatabaseConnectorServiceInitializationRuntimeException;
import com.qthegamep.pattern.project2.exception.runtime.CloseRedisClustersDatabaseConnectorServiceInitializationRuntimeException;
import com.qthegamep.pattern.project2.exception.runtime.RedisDatabaseConnectorServiceInitializationRuntimeException;
import com.qthegamep.pattern.project2.exception.runtime.SyncMongoDatabaseConnectorServiceInitializationRuntimeException;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.bson.codecs.configuration.CodecRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DatabaseConnectorServiceImpl implements DatabaseConnectorService {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseConnectorServiceImpl.class);

    private List<com.mongodb.MongoClient> syncMongoClients = new CopyOnWriteArrayList<>();
    private List<com.mongodb.async.client.MongoClient> asyncMongoClients = new CopyOnWriteArrayList<>();
    private List<JedisPool> redisPools = new CopyOnWriteArrayList<>();
    private List<JedisCluster> redisClusters = new CopyOnWriteArrayList<>();

    private String syncMongoDbStandaloneHost;
    private String syncMongoDbStandalonePort;
    private String syncMongoDbStandaloneUser;
    private String syncMongoDbStandaloneDb;
    private String syncMongoDbStandalonePassword;
    private String syncMongoDbClusterPort;
    private String syncMongoDbClusterUser;
    private String syncMongoDbClusterDb;
    private String syncMongoDbClusterPassword;
    private String asyncMongoDbStandaloneHost;
    private String asyncMongoDbStandalonePort;
    private String asyncMongoDbStandaloneUser;
    private String asyncMongoDbStandaloneDb;
    private String asyncMongoDbStandalonePassword;
    private String asyncMongoDbClusterPort;
    private String asyncMongoDbClusterUser;
    private String asyncMongoDbClusterDb;
    private String asyncMongoDbClusterPassword;
    private String redisPoolHost;
    private String redisPoolPort;
    private String redisPoolPassword;
    private String redisPoolMaxTotal;
    private String redisPoolMaxIdle;
    private String redisPoolTimeout;
    private String redisClusterPassword;
    private String redisClusterTestOnBorrow;
    private String redisClusterTestOnReturn;
    private String redisClusterMaxTotal;
    private String redisClusterMaxIdle;
    private String redisClusterMinIdle;
    private String redisClusterConnectionTimeout;
    private String redisClusterSoTimeout;
    private String redisClusterMaxAttempts;
    private ExitManagerService exitManagerService;

    @Inject
    public DatabaseConnectorServiceImpl(@Property(value = "sync.mongodb.standalone.host") String syncMongoDbStandaloneHost,
                                        @Property(value = "sync.mongodb.standalone.port") String syncMongoDbStandalonePort,
                                        @Property(value = "sync.mongodb.standalone.user") String syncMongoDbStandaloneUser,
                                        @Property(value = "sync.mongodb.standalone.db") String syncMongoDbStandaloneDb,
                                        @Property(value = "sync.mongodb.standalone.pass") String syncMongoDbStandalonePassword,
                                        @Property(value = "sync.mongodb.cluster.port") String syncMongoDbClusterPort,
                                        @Property(value = "sync.mongodb.cluster.user") String syncMongoDbClusterUser,
                                        @Property(value = "sync.mongodb.cluster.db") String syncMongoDbClusterDb,
                                        @Property(value = "sync.mongodb.cluster.pass") String syncMongoDbClusterPassword,
                                        @Property(value = "async.mongodb.standalone.host") String asyncMongoDbStandaloneHost,
                                        @Property(value = "async.mongodb.standalone.port") String asyncMongoDbStandalonePort,
                                        @Property(value = "async.mongodb.standalone.user") String asyncMongoDbStandaloneUser,
                                        @Property(value = "async.mongodb.standalone.db") String asyncMongoDbStandaloneDb,
                                        @Property(value = "async.mongodb.standalone.pass") String asyncMongoDbStandalonePassword,
                                        @Property(value = "async.mongodb.cluster.port") String asyncMongoDbClusterPort,
                                        @Property(value = "async.mongodb.cluster.user") String asyncMongoDbClusterUser,
                                        @Property(value = "async.mongodb.cluster.db") String asyncMongoDbClusterDb,
                                        @Property(value = "async.mongodb.cluster.pass") String asyncMongoDbClusterPassword,
                                        @Property(value = "redis.pool.host") String redisPoolHost,
                                        @Property(value = "redis.pool.port") String redisPoolPort,
                                        @Property(value = "redis.pool.password") String redisPoolPassword,
                                        @Property(value = "redis.pool.max.total") String redisPoolMaxTotal,
                                        @Property(value = "redis.pool.max.idle") String redisPoolMaxIdle,
                                        @Property(value = "redis.pool.timeout") String redisPoolTimeout,
                                        @Property(value = "redis.cluster.password") String redisClusterPassword,
                                        @Property(value = "redis.cluster.test.on.borrow") String redisClusterTestOnBorrow,
                                        @Property(value = "redis.cluster.test.on.return") String redisClusterTestOnReturn,
                                        @Property(value = "redis.cluster.max.total") String redisClusterMaxTotal,
                                        @Property(value = "redis.cluster.max.idle") String redisClusterMaxIdle,
                                        @Property(value = "redis.cluster.min.idle") String redisClusterMinIdle,
                                        @Property(value = "redis.cluster.connection.timeout") String redisClusterConnectionTimeout,
                                        @Property(value = "redis.cluster.so.timeout") String redisClusterSoTimeout,
                                        @Property(value = "redis.cluster.max.attempts") String redisClusterMaxAttempts,
                                        ExitManagerService exitManagerService) {
        this.syncMongoDbStandaloneHost = syncMongoDbStandaloneHost;
        this.syncMongoDbStandalonePort = syncMongoDbStandalonePort;
        this.syncMongoDbStandaloneUser = syncMongoDbStandaloneUser;
        this.syncMongoDbStandaloneDb = syncMongoDbStandaloneDb;
        this.syncMongoDbStandalonePassword = syncMongoDbStandalonePassword;
        this.syncMongoDbClusterPort = syncMongoDbClusterPort;
        this.syncMongoDbClusterUser = syncMongoDbClusterUser;
        this.syncMongoDbClusterDb = syncMongoDbClusterDb;
        this.syncMongoDbClusterPassword = syncMongoDbClusterPassword;
        this.asyncMongoDbStandaloneHost = asyncMongoDbStandaloneHost;
        this.asyncMongoDbStandalonePort = asyncMongoDbStandalonePort;
        this.asyncMongoDbStandaloneUser = asyncMongoDbStandaloneUser;
        this.asyncMongoDbStandaloneDb = asyncMongoDbStandaloneDb;
        this.asyncMongoDbStandalonePassword = asyncMongoDbStandalonePassword;
        this.asyncMongoDbClusterPort = asyncMongoDbClusterPort;
        this.asyncMongoDbClusterUser = asyncMongoDbClusterUser;
        this.asyncMongoDbClusterDb = asyncMongoDbClusterDb;
        this.asyncMongoDbClusterPassword = asyncMongoDbClusterPassword;
        this.redisPoolHost = redisPoolHost;
        this.redisPoolPort = redisPoolPort;
        this.redisPoolPassword = redisPoolPassword;
        this.redisPoolMaxTotal = redisPoolMaxTotal;
        this.redisPoolMaxIdle = redisPoolMaxIdle;
        this.redisPoolTimeout = redisPoolTimeout;
        this.redisClusterPassword = redisClusterPassword;
        this.redisClusterTestOnBorrow = redisClusterTestOnBorrow;
        this.redisClusterTestOnReturn = redisClusterTestOnReturn;
        this.redisClusterMaxTotal = redisClusterMaxTotal;
        this.redisClusterMaxIdle = redisClusterMaxIdle;
        this.redisClusterMinIdle = redisClusterMinIdle;
        this.redisClusterConnectionTimeout = redisClusterConnectionTimeout;
        this.redisClusterSoTimeout = redisClusterSoTimeout;
        this.redisClusterMaxAttempts = redisClusterMaxAttempts;
        this.exitManagerService = exitManagerService;
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB() {
        return connectToSyncMongoDB(null, null, null, MongoConnection.STANDALONE_MONGO_CONNECTION);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(MongoConnection connectionType) {
        return connectToSyncMongoDB(null, null, null, connectionType);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener) {
        return connectToSyncMongoDB(commandListener, null, null, MongoConnection.STANDALONE_MONGO_CONNECTION);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener, MongoConnection connectionType) {
        return connectToSyncMongoDB(commandListener, null, null, connectionType);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(ConnectionPoolListener connectionPoolListener) {
        return connectToSyncMongoDB(null, connectionPoolListener, null, MongoConnection.STANDALONE_MONGO_CONNECTION);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(ConnectionPoolListener connectionPoolListener, MongoConnection connectionType) {
        return connectToSyncMongoDB(null, connectionPoolListener, null, connectionType);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(CodecRegistry codecRegistry) {
        return connectToSyncMongoDB(null, null, codecRegistry, MongoConnection.STANDALONE_MONGO_CONNECTION);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(CodecRegistry codecRegistry, MongoConnection connectionType) {
        return connectToSyncMongoDB(null, null, codecRegistry, connectionType);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener) {
        return connectToSyncMongoDB(commandListener, connectionPoolListener, null, MongoConnection.STANDALONE_MONGO_CONNECTION);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, MongoConnection connectionType) {
        return connectToSyncMongoDB(commandListener, connectionPoolListener, null, connectionType);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, CodecRegistry codecRegistry, MongoConnection connectionType) {
        LOG.debug("Sync MongoDB type: {}", connectionType);
        try {
            switch (connectionType) {
                case CLUSTER_MONGO_CONNECTION:
                    return connectToClusterSyncMongoDB(commandListener, connectionPoolListener, codecRegistry);
                case STANDALONE_MONGO_CONNECTION:
                default:
                    return connectToStandaloneSyncMongoDB(commandListener, connectionPoolListener, codecRegistry);
            }
        } catch (Exception e) {
            throw new SyncMongoDatabaseConnectorServiceInitializationRuntimeException(e);
        }
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB() {
        return connectToAsyncMongoDB(null, null, null, MongoConnection.STANDALONE_MONGO_CONNECTION);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(MongoConnection connectionType) {
        return connectToAsyncMongoDB(null, null, null, connectionType);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener) {
        return connectToAsyncMongoDB(commandListener, null, null, MongoConnection.STANDALONE_MONGO_CONNECTION);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener, MongoConnection connectionType) {
        return connectToAsyncMongoDB(commandListener, null, null, connectionType);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(ConnectionPoolListener connectionPoolListener) {
        return connectToAsyncMongoDB(null, connectionPoolListener, null, MongoConnection.STANDALONE_MONGO_CONNECTION);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(ConnectionPoolListener connectionPoolListener, MongoConnection connectionType) {
        return connectToAsyncMongoDB(null, connectionPoolListener, null, connectionType);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CodecRegistry codecRegistry) {
        return connectToAsyncMongoDB(null, null, codecRegistry, MongoConnection.STANDALONE_MONGO_CONNECTION);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CodecRegistry codecRegistry, MongoConnection connectionType) {
        return connectToAsyncMongoDB(null, null, codecRegistry, connectionType);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener) {
        return connectToAsyncMongoDB(commandListener, connectionPoolListener, null, MongoConnection.STANDALONE_MONGO_CONNECTION);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, MongoConnection connectionType) {
        return connectToAsyncMongoDB(commandListener, connectionPoolListener, null, connectionType);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, CodecRegistry codecRegistry, MongoConnection connectionType) {
        try {
            switch (connectionType) {
                case CLUSTER_MONGO_CONNECTION:
                    return connectToClusterAsyncMongoDB(commandListener, connectionPoolListener, codecRegistry);
                case STANDALONE_MONGO_CONNECTION:
                default:
                    return connectToStandaloneAsyncMongoDB(commandListener, connectionPoolListener, codecRegistry);
            }
        } catch (Exception e) {
            throw new AsyncMongoDatabaseConnectorServiceInitializationRuntimeException(e);
        }
    }

    @Override
    public JedisPool connectToPoolRedis() {
        try {
            LOG.debug("Pool Redis properties -> Host: {} Port: {} Password: {} Max Total: {} Max Idle: {} Timeout: {}", redisPoolHost, redisPoolPort, redisPoolPassword, redisPoolMaxTotal, redisPoolMaxIdle, redisPoolTimeout);
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(Integer.parseInt(redisPoolMaxTotal));
            jedisPoolConfig.setMaxIdle(Integer.parseInt(redisPoolMaxIdle));
            JedisPool jedisPool = createJedisPool(jedisPoolConfig);
            checkRedisPoolConnection(jedisPool);
            LOG.info("Pool Redis {}:{} was connected", redisPoolHost, redisPoolPort);
            redisPools.add(jedisPool);
            return jedisPool;
        } catch (Exception e) {
            throw new RedisDatabaseConnectorServiceInitializationRuntimeException(e);
        }
    }

    @Override
    public JedisCluster connectToClusterRedis() {
        try {
            List<String> hosts = getRedisListOfHosts();
            List<String> ports = getRedisListOfPorts();
            LOG.debug("Cluster Redis properties -> Host: {} Port: {} Password: {} Test On Borrow: {} Test On Return: {} Max Total: {} Max Idle: {} Min Idle: {} Connection Timeout: {} So Timeout: {} Max Attempts: {}", hosts, ports, redisClusterPassword, redisClusterTestOnBorrow, redisClusterTestOnReturn, redisClusterMaxTotal, redisClusterMaxIdle, redisClusterMinIdle, redisClusterConnectionTimeout, redisClusterSoTimeout, redisClusterMaxAttempts);
            Set<HostAndPort> clusterRedisNodes = buildClusterRedisNodes(hosts, ports);
            GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
            genericObjectPoolConfig.setTestOnBorrow(Boolean.parseBoolean(redisClusterTestOnBorrow));
            genericObjectPoolConfig.setTestOnReturn(Boolean.parseBoolean(redisClusterTestOnReturn));
            genericObjectPoolConfig.setMaxTotal(Integer.parseInt(redisClusterMaxTotal));
            genericObjectPoolConfig.setMaxIdle(Integer.parseInt(redisClusterMaxIdle));
            genericObjectPoolConfig.setMinIdle(Integer.parseInt(redisClusterMinIdle));
            JedisCluster jedisCluster = createJedisCluster(clusterRedisNodes, genericObjectPoolConfig);
            LOG.info("Cluster Redis were connected:");
            IntStream.range(0, hosts.size())
                    .forEach(i -> LOG.info("{}:{}", hosts.get(i), ports.get(i)));
            redisClusters.add(jedisCluster);
            return jedisCluster;
        } catch (Exception e) {
            throw new RedisDatabaseConnectorServiceInitializationRuntimeException(e);
        }
    }

    @Override
    public List<com.mongodb.MongoClient> getSyncMongoDBConnections() {
        return syncMongoClients;
    }

    @Override
    public List<com.mongodb.async.client.MongoClient> getAsyncMongoDBConnections() {
        return asyncMongoClients;
    }

    @Override
    public List<JedisPool> getRedisPools() {
        return redisPools;
    }

    @Override
    public List<JedisCluster> getRedisClusters() {
        return redisClusters;
    }

    @Override
    public void closeSyncMongoDBConnections() {
        LOG.info("Sync MongoDB's to close: {}", syncMongoClients.size());
        syncMongoClients.forEach(Mongo::close);
        syncMongoClients.clear();
    }

    @Override
    public void closeAsyncMongoDBConnections() {
        LOG.info("Async MongoDB's to close: {}", asyncMongoClients.size());
        asyncMongoClients.forEach(com.mongodb.async.client.MongoClient::close);
        asyncMongoClients.clear();
    }

    @Override
    public void closeRedisPools() {
        LOG.info("Redis pools to close: {}", redisPools.size());
        redisPools.forEach(JedisPool::close);
        redisPools.clear();
    }

    @Override
    public void closeRedisClusters() {
        try {
            LOG.info("Redis clusters to close: {}", redisClusters.size());
            for (JedisCluster redisCluster : redisClusters) {
                redisCluster.close();
            }
            redisClusters.clear();
        } catch (Exception e) {
            throw new CloseRedisClustersDatabaseConnectorServiceInitializationRuntimeException(e);
        }
    }

    @Override
    public void closeAll() {
        LOG.info("Close all database connections");
        closeSyncMongoDBConnections();
        closeAsyncMongoDBConnections();
        closeRedisPools();
        closeRedisClusters();
    }

    private com.mongodb.client.MongoDatabase connectToStandaloneSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, CodecRegistry codecRegistry) {
        LOG.debug("Standalone sync MongoDB properties -> Host: {} Port: {} User: {} DB: {} Password: {}", syncMongoDbStandaloneHost, syncMongoDbStandalonePort, syncMongoDbStandaloneUser, syncMongoDbStandaloneDb, syncMongoDbStandalonePassword);
        ServerAddress serverAddress = new ServerAddress(syncMongoDbStandaloneHost, Integer.parseInt(syncMongoDbStandalonePort));
        MongoClientOptions mongoClientOptions = buildSyncMongoClientOptions(commandListener, connectionPoolListener, codecRegistry);
        com.mongodb.MongoClient mongoClient = createStandaloneSyncMongoClient(serverAddress, mongoClientOptions);
        com.mongodb.client.MongoDatabase database = mongoClient.getDatabase(syncMongoDbStandaloneDb);
        checkSyncMongoDBConnection(database);
        LOG.info("Standalone sync MongoDB {}:{} was connected", syncMongoDbStandaloneHost, syncMongoDbStandalonePort);
        syncMongoClients.add(mongoClient);
        return database;
    }

    private com.mongodb.client.MongoDatabase connectToClusterSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, CodecRegistry codecRegistry) {
        List<String> hosts = getSyncMongoDBListOfHosts();
        LOG.debug("Cluster sync MongoDB properties -> Host: {} Port: {} User: {} DB: {} Password: {}", hosts, syncMongoDbClusterPort, syncMongoDbClusterUser, syncMongoDbClusterDb, syncMongoDbClusterPassword);
        List<ServerAddress> serverAddresses = hosts.stream()
                .map(host -> new ServerAddress(host, Integer.parseInt(syncMongoDbClusterPort)))
                .collect(Collectors.toList());
        MongoClientOptions mongoClientOptions = buildSyncMongoClientOptions(commandListener, connectionPoolListener, codecRegistry);
        com.mongodb.MongoClient mongoClient = createdClusterSyncMongoClient(serverAddresses, mongoClientOptions);
        com.mongodb.client.MongoDatabase database = mongoClient.getDatabase(syncMongoDbClusterDb);
        checkSyncMongoDBConnection(database);
        LOG.info("Cluster sync MongoDB were connected:");
        hosts.forEach(host -> LOG.info("{}:{}", host, syncMongoDbClusterPort));
        syncMongoClients.add(mongoClient);
        return database;
    }

    private com.mongodb.async.client.MongoDatabase connectToStandaloneAsyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, CodecRegistry codecRegistry) {
        LOG.debug("Standalone async MongoDB properties -> Host: {} Port: {} User: {} DB: {} Password: {}", asyncMongoDbStandaloneHost, asyncMongoDbStandalonePort, asyncMongoDbStandaloneUser, asyncMongoDbStandaloneDb, asyncMongoDbStandalonePassword);
        ServerAddress serverAddress = new ServerAddress(asyncMongoDbStandaloneHost, Integer.parseInt(asyncMongoDbStandalonePort));
        ClusterSettings clusterSettings = ClusterSettings.builder()
                .hosts(Collections.singletonList(serverAddress))
                .build();
        MongoClientSettings.Builder mongoClientSettingsBuilder = buildAsyncMongoClientSettingsBuilder(commandListener, connectionPoolListener, codecRegistry, clusterSettings);
        com.mongodb.async.client.MongoClient mongoClient = createStandaloneAsyncMongoClient(mongoClientSettingsBuilder);
        com.mongodb.async.client.MongoDatabase database = mongoClient.getDatabase(asyncMongoDbStandaloneDb);
        checkAsyncMongoDBConnection(database);
        LOG.info("Standalone async MongoDB {}:{} was connected", asyncMongoDbStandaloneHost, asyncMongoDbStandalonePort);
        asyncMongoClients.add(mongoClient);
        return database;
    }

    private com.mongodb.async.client.MongoDatabase connectToClusterAsyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, CodecRegistry codecRegistry) {
        List<String> hosts = getAsyncMongoDBListOfHosts();
        LOG.debug("Cluster async MongoDB properties -> Host: {} Port: {} User: {} DB: {} Password: {}", hosts, asyncMongoDbClusterPort, asyncMongoDbClusterUser, asyncMongoDbClusterDb, asyncMongoDbClusterPassword);
        List<ServerAddress> serverAddresses = hosts.stream()
                .map(host -> new ServerAddress(host, Integer.parseInt(asyncMongoDbClusterPort)))
                .collect(Collectors.toList());
        ClusterSettings clusterSettings = ClusterSettings.builder()
                .hosts(serverAddresses)
                .build();
        MongoClientSettings.Builder mongoClientSettingsBuilder = buildAsyncMongoClientSettingsBuilder(commandListener, connectionPoolListener, codecRegistry, clusterSettings);
        com.mongodb.async.client.MongoClient mongoClient = createClusterAsyncMongoClient(mongoClientSettingsBuilder);
        com.mongodb.async.client.MongoDatabase database = mongoClient.getDatabase(asyncMongoDbClusterDb);
        checkAsyncMongoDBConnection(database);
        LOG.info("Cluster async MongoDB were connected:");
        hosts.forEach(host -> LOG.info("{}:{}", host, asyncMongoDbClusterPort));
        asyncMongoClients.add(mongoClient);
        return database;
    }

    private com.mongodb.MongoClient createStandaloneSyncMongoClient(ServerAddress serverAddress, MongoClientOptions mongoClientOptions) {
        if (Strings.isNullOrEmpty(syncMongoDbStandaloneUser)) {
            return new com.mongodb.MongoClient(serverAddress, mongoClientOptions);
        } else {
            MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(syncMongoDbStandaloneUser, syncMongoDbStandaloneDb, syncMongoDbStandalonePassword.toCharArray());
            return new com.mongodb.MongoClient(serverAddress, Collections.singletonList(mongoCredential), mongoClientOptions);
        }
    }

    private com.mongodb.MongoClient createdClusterSyncMongoClient(List<ServerAddress> serverAddresses, MongoClientOptions mongoClientOptions) {
        if (Strings.isNullOrEmpty(syncMongoDbClusterUser)) {
            return new com.mongodb.MongoClient(serverAddresses, mongoClientOptions);
        } else {
            MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(syncMongoDbClusterUser, syncMongoDbClusterDb, syncMongoDbClusterPassword.toCharArray());
            return new com.mongodb.MongoClient(serverAddresses, Collections.singletonList(mongoCredential), mongoClientOptions);
        }
    }

    private com.mongodb.async.client.MongoClient createStandaloneAsyncMongoClient(MongoClientSettings.Builder mongoClientSettingsBuilder) {
        MongoClientSettings mongoClientSettings;
        if (Strings.isNullOrEmpty(asyncMongoDbStandaloneUser)) {
            mongoClientSettings = mongoClientSettingsBuilder.build();
        } else {
            MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(asyncMongoDbStandaloneUser, asyncMongoDbStandaloneDb, asyncMongoDbStandalonePassword.toCharArray());
            mongoClientSettings = mongoClientSettingsBuilder.credentialList(Collections.singletonList(mongoCredential))
                    .build();
        }
        return MongoClients.create(mongoClientSettings);
    }

    private com.mongodb.async.client.MongoClient createClusterAsyncMongoClient(MongoClientSettings.Builder mongoClientSettingsBuilder) {
        MongoClientSettings mongoClientSettings;
        if (Strings.isNullOrEmpty(asyncMongoDbClusterUser)) {
            mongoClientSettings = mongoClientSettingsBuilder.build();
        } else {
            MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(asyncMongoDbClusterUser, asyncMongoDbClusterDb, asyncMongoDbClusterPassword.toCharArray());
            mongoClientSettings = mongoClientSettingsBuilder.credentialList(Collections.singletonList(mongoCredential))
                    .build();
        }
        return MongoClients.create(mongoClientSettings);
    }

    private JedisPool createJedisPool(JedisPoolConfig jedisPoolConfig) {
        if (redisPoolPassword == null || redisPoolPassword.isEmpty()) {
            return new JedisPool(jedisPoolConfig, redisPoolHost, Integer.parseInt(redisPoolPort), Integer.parseInt(redisPoolTimeout));
        } else {
            return new JedisPool(jedisPoolConfig, redisPoolHost, Integer.parseInt(redisPoolPort), Integer.parseInt(redisPoolTimeout), redisPoolPassword);
        }
    }

    private JedisCluster createJedisCluster(Set<HostAndPort> clusterRedisNodes, GenericObjectPoolConfig genericObjectPoolConfig) {
        if (redisClusterPassword == null || redisClusterPassword.isEmpty()) {
            return new JedisCluster(clusterRedisNodes, Integer.parseInt(redisClusterConnectionTimeout), Integer.parseInt(redisClusterSoTimeout), Integer.parseInt(redisClusterMaxAttempts), genericObjectPoolConfig);
        } else {
            return new JedisCluster(clusterRedisNodes, Integer.parseInt(redisClusterConnectionTimeout), Integer.parseInt(redisClusterSoTimeout), Integer.parseInt(redisClusterMaxAttempts), redisClusterPassword, genericObjectPoolConfig);
        }
    }

    private MongoClientOptions buildSyncMongoClientOptions(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, CodecRegistry codecRegistry) {
        MongoClientOptions.Builder mongoClientOptionsBuilder = MongoClientOptions.builder();
        if (commandListener != null) {
            mongoClientOptionsBuilder = mongoClientOptionsBuilder.addCommandListener(commandListener);
        }
        if (connectionPoolListener != null) {
            mongoClientOptionsBuilder = mongoClientOptionsBuilder.addConnectionPoolListener(connectionPoolListener);
        }
        if (codecRegistry != null) {
            mongoClientOptionsBuilder = mongoClientOptionsBuilder.codecRegistry(codecRegistry);
        }
        return mongoClientOptionsBuilder.build();
    }

    private MongoClientSettings.Builder buildAsyncMongoClientSettingsBuilder(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, CodecRegistry codecRegistry, ClusterSettings clusterSettings) {
        MongoClientSettings.Builder mongoClientSettingsBuilder = MongoClientSettings.builder();
        if (commandListener != null) {
            mongoClientSettingsBuilder.addCommandListener(commandListener);
        }
        if (connectionPoolListener != null) {
            ConnectionPoolSettings connectionPoolSettings = ConnectionPoolSettings.builder()
                    .addConnectionPoolListener(connectionPoolListener)
                    .build();
            mongoClientSettingsBuilder.connectionPoolSettings(connectionPoolSettings);
        }
        if (codecRegistry != null) {
            mongoClientSettingsBuilder.codecRegistry(codecRegistry);
        }
        return mongoClientSettingsBuilder.clusterSettings(clusterSettings);
    }

    private Set<HostAndPort> buildClusterRedisNodes(List<String> hosts, List<String> ports) {
        Set<HostAndPort> redisClusterNodes = new HashSet<>();
        for (int i = 0; i < hosts.size(); i++) {
            String host = hosts.get(i);
            int port = Integer.parseInt(ports.get(i));
            HostAndPort hostAndPort = new HostAndPort(host, port);
            redisClusterNodes.add(hostAndPort);
        }
        return redisClusterNodes;
    }

    private List<String> getSyncMongoDBListOfHosts() {
        return getListOf("sync.mongodb.cluster.host");
    }

    private List<String> getAsyncMongoDBListOfHosts() {
        return getListOf("async.mongodb.cluster.host");
    }

    private List<String> getRedisListOfHosts() {
        return getListOf("redis.cluster.host");
    }

    private List<String> getRedisListOfPorts() {
        return getListOf("redis.cluster.port");
    }

    private List<String> getListOf(String property) {
        List<String> hosts = new ArrayList<>();
        int i = 1;
        while (true) {
            String host = System.getProperty(property + "." + i);
            if (Strings.isNullOrEmpty(host)) {
                break;
            } else {
                hosts.add(host);
                i++;
            }
        }
        return hosts;
    }

    private void checkSyncMongoDBConnection(com.mongodb.client.MongoDatabase database) {
        database.listCollectionNames().iterator();
    }

    private void checkAsyncMongoDBConnection(com.mongodb.async.client.MongoDatabase database) {
        database.listCollectionNames().forEach(
                document -> {
                },
                new ShutdownServerCallback(exitManagerService));
    }

    private void checkRedisPoolConnection(JedisPool redisPool) {
        try (Jedis jedis = redisPool.getResource()) {
            jedis.isConnected();
        }
    }
}
