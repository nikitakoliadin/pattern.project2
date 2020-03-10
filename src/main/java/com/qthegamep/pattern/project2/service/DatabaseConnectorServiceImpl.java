package com.qthegamep.pattern.project2.service;

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
import com.qthegamep.pattern.project2.repository.mongo.callback.ShutdownServerCallback;
import com.qthegamep.pattern.project2.exception.runtime.AsyncMongoDatabaseConnectorServiceRuntimeException;
import com.qthegamep.pattern.project2.exception.runtime.CloseRedisClustersDatabaseConnectorServiceRuntimeException;
import com.qthegamep.pattern.project2.exception.runtime.RedisDatabaseConnectorServiceRuntimeException;
import com.qthegamep.pattern.project2.exception.runtime.SyncMongoDatabaseConnectorServiceRuntimeException;
import com.qthegamep.pattern.project2.model.container.Error;
import com.qthegamep.pattern.project2.util.Constants;
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
    private ExitManagerService exitManagerService;

    @Inject
    public DatabaseConnectorServiceImpl(@Property(value = "redis.pool.host") String redisPoolHost,
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
                                        @Property(value = "sync.mongodb.standalone.host") String syncMongoDbStandaloneHost,
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
                                        ExitManagerService exitManagerService) {
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
        this.exitManagerService = exitManagerService;
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB() {
        return connectToSyncMongoDB(null, null, null, Constants.STANDALONE_MONGO_DB_TYPE);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(String connectionType) {
        return connectToSyncMongoDB(null, null, null, connectionType);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener) {
        return connectToSyncMongoDB(commandListener, null, null, Constants.STANDALONE_MONGO_DB_TYPE);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener, String connectionType) {
        return connectToSyncMongoDB(commandListener, null, null, connectionType);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(ConnectionPoolListener connectionPoolListener) {
        return connectToSyncMongoDB(null, connectionPoolListener, null, Constants.STANDALONE_MONGO_DB_TYPE);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(ConnectionPoolListener connectionPoolListener, String connectionType) {
        return connectToSyncMongoDB(null, connectionPoolListener, null, connectionType);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(CodecRegistry codecRegistry) {
        return connectToSyncMongoDB(null, null, codecRegistry, Constants.STANDALONE_MONGO_DB_TYPE);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(CodecRegistry codecRegistry, String connectionType) {
        return connectToSyncMongoDB(null, null, codecRegistry, connectionType);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener) {
        return connectToSyncMongoDB(commandListener, connectionPoolListener, null, Constants.STANDALONE_MONGO_DB_TYPE);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, String connectionType) {
        return connectToSyncMongoDB(commandListener, connectionPoolListener, null, connectionType);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, CodecRegistry codecRegistry, String connectionType) {
        LOG.debug("Sync MongoDB type: {}", connectionType);
        try {
            if (Constants.STANDALONE_MONGO_DB_TYPE.equalsIgnoreCase(connectionType)) {
                return connectToStandaloneSyncMongoDB(commandListener, connectionPoolListener, codecRegistry);
            } else if (Constants.CLUSTER_MONGO_DB_TYPE.equalsIgnoreCase(connectionType)) {
                return connectToClusterSyncMongoDB(commandListener, connectionPoolListener, codecRegistry);
            } else {
                throw new SyncMongoDatabaseConnectorServiceRuntimeException(Error.MONGO_DB_NOT_EXISTING_TYPE_ERROR);
            }
        } catch (SyncMongoDatabaseConnectorServiceRuntimeException e) {
            throw new SyncMongoDatabaseConnectorServiceRuntimeException(e, e.getError());
        } catch (Exception e) {
            throw new SyncMongoDatabaseConnectorServiceRuntimeException(e, Error.SYNC_MONGO_DB_CONNECTOR_ERROR);
        }
    }

    @Override
    public List<com.mongodb.MongoClient> getSyncMongoDBConnections() {
        return syncMongoClients;
    }

    @Override
    public void closeSyncMongoDBConnections() {
        LOG.info("Sync MongoDB's to close: {}", syncMongoClients.size());
        syncMongoClients.forEach(Mongo::close);
        syncMongoClients.clear();
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB() {
        return connectToAsyncMongoDB(null, null, null, Constants.STANDALONE_MONGO_DB_TYPE);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(String connectionType) {
        return connectToAsyncMongoDB(null, null, null, connectionType);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener) {
        return connectToAsyncMongoDB(commandListener, null, null, Constants.STANDALONE_MONGO_DB_TYPE);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener, String connectionType) {
        return connectToAsyncMongoDB(commandListener, null, null, connectionType);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(ConnectionPoolListener connectionPoolListener) {
        return connectToAsyncMongoDB(null, connectionPoolListener, null, Constants.STANDALONE_MONGO_DB_TYPE);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(ConnectionPoolListener connectionPoolListener, String connectionType) {
        return connectToAsyncMongoDB(null, connectionPoolListener, null, connectionType);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CodecRegistry codecRegistry) {
        return connectToAsyncMongoDB(null, null, codecRegistry, Constants.STANDALONE_MONGO_DB_TYPE);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CodecRegistry codecRegistry, String connectionType) {
        return connectToAsyncMongoDB(null, null, codecRegistry, connectionType);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener) {
        return connectToAsyncMongoDB(commandListener, connectionPoolListener, null, Constants.STANDALONE_MONGO_DB_TYPE);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, String connectionType) {
        return connectToAsyncMongoDB(commandListener, connectionPoolListener, null, connectionType);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, CodecRegistry codecRegistry, String connectionType) {
        try {
            if (Constants.STANDALONE_MONGO_DB_TYPE.equalsIgnoreCase(connectionType)) {
                return connectToStandaloneAsyncMongoDB(commandListener, connectionPoolListener, codecRegistry);
            } else if (Constants.CLUSTER_MONGO_DB_TYPE.equalsIgnoreCase(connectionType)) {
                return connectToClusterAsyncMongoDB(commandListener, connectionPoolListener, codecRegistry);
            } else {
                throw new AsyncMongoDatabaseConnectorServiceRuntimeException(Error.MONGO_DB_NOT_EXISTING_TYPE_ERROR);
            }
        } catch (AsyncMongoDatabaseConnectorServiceRuntimeException e) {
            throw new AsyncMongoDatabaseConnectorServiceRuntimeException(e, e.getError());
        } catch (Exception e) {
            throw new AsyncMongoDatabaseConnectorServiceRuntimeException(e, Error.ASYNC_MONGO_DB_CONNECTOR_ERROR);
        }
    }

    @Override
    public List<com.mongodb.async.client.MongoClient> getAsyncMongoDBConnections() {
        return asyncMongoClients;
    }

    @Override
    public void closeAsyncMongoDBConnections() {
        LOG.info("Async MongoDB's to close: {}", asyncMongoClients.size());
        asyncMongoClients.forEach(com.mongodb.async.client.MongoClient::close);
        asyncMongoClients.clear();
    }

    @Override
    public JedisPool connectToPoolRedis() {
        try {
            LOG.debug("Pool Redis properties -> Host: {} Port: {} Password: {} Max Total: {} Max Idle: {} Timeout: {}", redisPoolHost, redisPoolPort, redisPoolPassword, redisPoolMaxTotal, redisPoolMaxIdle, redisPoolTimeout);
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(Integer.parseInt(redisPoolMaxTotal));
            jedisPoolConfig.setMaxIdle(Integer.parseInt(redisPoolMaxIdle));
            JedisPool jedisPool;
            if (redisPoolPassword == null || redisPoolPassword.isEmpty()) {
                jedisPool = new JedisPool(jedisPoolConfig, redisPoolHost, Integer.parseInt(redisPoolPort), Integer.parseInt(redisPoolTimeout));
            } else {
                jedisPool = new JedisPool(jedisPoolConfig, redisPoolHost, Integer.parseInt(redisPoolPort), Integer.parseInt(redisPoolTimeout), redisPoolPassword);
            }
            checkRedisPoolConnection(jedisPool);
            LOG.info("Pool Redis {}:{} was connected", redisPoolHost, redisPoolPort);
            redisPools.add(jedisPool);
            return jedisPool;
        } catch (Exception e) {
            throw new RedisDatabaseConnectorServiceRuntimeException(e, Error.REDIS_POOL_CONNECTOR_ERROR);
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
            JedisCluster jedisCluster;
            if (redisClusterPassword == null || redisClusterPassword.isEmpty()) {
                jedisCluster = new JedisCluster(clusterRedisNodes, Integer.parseInt(redisClusterConnectionTimeout), Integer.parseInt(redisClusterSoTimeout), Integer.parseInt(redisClusterMaxAttempts), genericObjectPoolConfig);
            } else {
                jedisCluster = new JedisCluster(clusterRedisNodes, Integer.parseInt(redisClusterConnectionTimeout), Integer.parseInt(redisClusterSoTimeout), Integer.parseInt(redisClusterMaxAttempts), redisClusterPassword, genericObjectPoolConfig);
            }
            LOG.info("Cluster Redis were connected:");
            IntStream.range(0, hosts.size())
                    .forEach(i -> LOG.info("{}:{}", hosts.get(i), ports.get(i)));
            redisClusters.add(jedisCluster);
            return jedisCluster;
        } catch (Exception e) {
            throw new RedisDatabaseConnectorServiceRuntimeException(e, Error.REDIS_CLUSTER_CONNECTOR_ERROR);
        }
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
            throw new CloseRedisClustersDatabaseConnectorServiceRuntimeException(e, Error.CLOSE_CLUSTER_REDIS_ERROR);
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
        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(syncMongoDbStandaloneUser, syncMongoDbStandaloneDb, syncMongoDbStandalonePassword.toCharArray());
        MongoClientOptions mongoClientOptions = buildSyncMongoClientOptions(commandListener, connectionPoolListener, codecRegistry);
        com.mongodb.MongoClient mongoClient = new com.mongodb.MongoClient(serverAddress, Collections.singletonList(mongoCredential), mongoClientOptions);
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
        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(syncMongoDbClusterUser, syncMongoDbClusterDb, syncMongoDbClusterPassword.toCharArray());
        MongoClientOptions mongoClientOptions = buildSyncMongoClientOptions(commandListener, connectionPoolListener, codecRegistry);
        com.mongodb.MongoClient mongoClient = new com.mongodb.MongoClient(serverAddresses, Collections.singletonList(mongoCredential), mongoClientOptions);
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
        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(asyncMongoDbStandaloneUser, asyncMongoDbStandaloneDb, asyncMongoDbStandalonePassword.toCharArray());
        ClusterSettings clusterSettings = ClusterSettings.builder()
                .hosts(Collections.singletonList(serverAddress))
                .build();
        MongoClientSettings mongoClientSettings = buildAsyncMongoClientSettings(commandListener, connectionPoolListener, codecRegistry, mongoCredential, clusterSettings);
        com.mongodb.async.client.MongoClient mongoClient = MongoClients.create(mongoClientSettings);
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
        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(asyncMongoDbClusterUser, asyncMongoDbClusterDb, asyncMongoDbClusterPassword.toCharArray());
        ClusterSettings clusterSettings = ClusterSettings.builder()
                .hosts(serverAddresses)
                .build();
        MongoClientSettings mongoClientSettings = buildAsyncMongoClientSettings(commandListener, connectionPoolListener, codecRegistry, mongoCredential, clusterSettings);
        com.mongodb.async.client.MongoClient mongoClient = MongoClients.create(mongoClientSettings);
        com.mongodb.async.client.MongoDatabase database = mongoClient.getDatabase(asyncMongoDbClusterDb);
        checkAsyncMongoDBConnection(database);
        LOG.info("Cluster async MongoDB were connected:");
        hosts.forEach(host -> LOG.info("{}:{}", host, asyncMongoDbClusterPort));
        asyncMongoClients.add(mongoClient);
        return database;
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

    private MongoClientSettings buildAsyncMongoClientSettings(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, CodecRegistry codecRegistry, MongoCredential mongoCredential, ClusterSettings clusterSettings) {
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
        return mongoClientSettingsBuilder.credentialList(Collections.singletonList(mongoCredential))
                .clusterSettings(clusterSettings)
                .build();
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
            if (host == null || host.isEmpty()) {
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
