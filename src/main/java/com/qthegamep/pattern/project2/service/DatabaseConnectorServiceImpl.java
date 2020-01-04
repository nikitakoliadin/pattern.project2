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
import com.qthegamep.pattern.project2.callback.MongoShutdownServerCallback;
import com.qthegamep.pattern.project2.exception.AsyncMongoDBConnectorRuntimeException;
import com.qthegamep.pattern.project2.exception.CloseClusterRedisRuntimeException;
import com.qthegamep.pattern.project2.exception.RedisConnectorRuntimeException;
import com.qthegamep.pattern.project2.exception.SyncMongoDBConnectorRuntimeException;
import com.qthegamep.pattern.project2.model.container.Error;
import com.qthegamep.pattern.project2.util.Constants;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

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

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB() {
        return connectToSyncMongoDB(null, null, Constants.STANDALONE_MONGO_DB_TYPE.getValue());
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(String connectionType) {
        return connectToSyncMongoDB(null, null, connectionType);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener) {
        return connectToSyncMongoDB(commandListener, null, Constants.STANDALONE_MONGO_DB_TYPE.getValue());
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener, String connectionType) {
        return connectToSyncMongoDB(commandListener, null, connectionType);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(ConnectionPoolListener connectionPoolListener) {
        return connectToSyncMongoDB(null, connectionPoolListener, Constants.STANDALONE_MONGO_DB_TYPE.getValue());
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(ConnectionPoolListener connectionPoolListener, String connectionType) {
        return connectToSyncMongoDB(null, connectionPoolListener, connectionType);
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener) {
        return connectToSyncMongoDB(commandListener, connectionPoolListener, Constants.STANDALONE_MONGO_DB_TYPE.getValue());
    }

    @Override
    public com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, String connectionType) {
        LOG.debug("Sync MongoDB type: {}", connectionType);
        try {
            if (Constants.STANDALONE_MONGO_DB_TYPE.getValue().equalsIgnoreCase(connectionType)) {
                return connectToStandaloneSyncMongoDB(commandListener, connectionPoolListener);
            } else if (Constants.CLUSTER_MONGO_DB_TYPE.getValue().equalsIgnoreCase(connectionType)) {
                return connectToClusterSyncMongoDB(commandListener, connectionPoolListener);
            } else {
                throw new SyncMongoDBConnectorRuntimeException(Error.MONGO_DB_NOT_EXISTING_TYPE_ERROR);
            }
        } catch (SyncMongoDBConnectorRuntimeException e) {
            throw new SyncMongoDBConnectorRuntimeException(e, e.getError());
        } catch (Exception e) {
            throw new SyncMongoDBConnectorRuntimeException(e, Error.SYNC_MONGO_DB_CONNECTOR_ERROR);
        }
    }

    @Override
    public List<com.mongodb.MongoClient> getSyncMongoDBConnections() {
        return syncMongoClients;
    }

    @Override
    public void closeSyncMongoDBConnections() {
        LOG.debug("Sync MongoDB's to close: {}", syncMongoClients.size());
        syncMongoClients.forEach(Mongo::close);
        syncMongoClients.clear();
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB() {
        return connectToAsyncMongoDB(null, null, Constants.STANDALONE_MONGO_DB_TYPE.getValue());
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(String connectionType) {
        return connectToAsyncMongoDB(null, null, connectionType);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener) {
        return connectToAsyncMongoDB(commandListener, null, Constants.STANDALONE_MONGO_DB_TYPE.getValue());
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener, String connectionType) {
        return connectToAsyncMongoDB(commandListener, null, connectionType);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(ConnectionPoolListener connectionPoolListener) {
        return connectToAsyncMongoDB(null, connectionPoolListener, Constants.STANDALONE_MONGO_DB_TYPE.getValue());
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(ConnectionPoolListener connectionPoolListener, String connectionType) {
        return connectToAsyncMongoDB(null, connectionPoolListener, connectionType);
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener) {
        return connectToAsyncMongoDB(commandListener, connectionPoolListener, Constants.STANDALONE_MONGO_DB_TYPE.getValue());
    }

    @Override
    public com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, String connectionType) {
        try {
            if (Constants.STANDALONE_MONGO_DB_TYPE.getValue().equalsIgnoreCase(connectionType)) {
                return connectToStandaloneAsyncMongoDB(commandListener, connectionPoolListener);
            } else if (Constants.CLUSTER_MONGO_DB_TYPE.getValue().equalsIgnoreCase(connectionType)) {
                return connectToClusterAsyncMongoDB(commandListener, connectionPoolListener);
            } else {
                throw new AsyncMongoDBConnectorRuntimeException(Error.MONGO_DB_NOT_EXISTING_TYPE_ERROR);
            }
        } catch (AsyncMongoDBConnectorRuntimeException e) {
            throw new AsyncMongoDBConnectorRuntimeException(e, e.getError());
        } catch (Exception e) {
            throw new AsyncMongoDBConnectorRuntimeException(e, Error.ASYNC_MONGO_DB_CONNECTOR_ERROR);
        }
    }

    @Override
    public List<com.mongodb.async.client.MongoClient> getAsyncMongoDBConnections() {
        return asyncMongoClients;
    }

    @Override
    public void closeAsyncMongoDBConnections() {
        LOG.debug("Async MongoDB's to close: {}", asyncMongoClients.size());
        asyncMongoClients.forEach(com.mongodb.async.client.MongoClient::close);
        asyncMongoClients.clear();
    }

    @Override
    public JedisPool connectToPoolRedis() {
        try {
            String host = System.getProperty("redis.pool.host");
            String port = System.getProperty("redis.pool.port");
            String password = System.getProperty("redis.pool.password");
            String maxTotal = System.getProperty("redis.pool.max.total");
            String maxIdle = System.getProperty("redis.pool.max.idle");
            String timeout = System.getProperty("redis.pool.timeout");
            LOG.debug("Pool Redis properties -> Host: {} Port: {} Password: {} Max Total: {} Max Idle: {} Timeout: {}", host, port, password, maxTotal, maxIdle, timeout);
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(Integer.parseInt(maxTotal));
            jedisPoolConfig.setMaxIdle(Integer.parseInt(maxIdle));
            JedisPool jedisPool;
            if (password == null || password.isEmpty()) {
                jedisPool = new JedisPool(jedisPoolConfig, host, Integer.parseInt(port), Integer.parseInt(timeout));
            } else {
                jedisPool = new JedisPool(jedisPoolConfig, host, Integer.parseInt(port), Integer.parseInt(timeout), password);
            }
            checkRedisPoolConnection(jedisPool);
            LOG.info("Pool Redis {}:{} was connected", host, port);
            redisPools.add(jedisPool);
            return jedisPool;
        } catch (Exception e) {
            throw new RedisConnectorRuntimeException(e, Error.REDIS_POOL_CONNECTOR_ERROR);
        }
    }

    @Override
    public JedisCluster connectToClusterRedis() {
        try {
            List<String> hosts = getRedisListOfHosts();
            List<String> ports = getRedisListOfPorts();
            String password = System.getProperty("redis.cluster.password");
            String testOnBorrow = System.getProperty("redis.cluster.test.on.borrow");
            String testOnReturn = System.getProperty("redis.cluster.test.on.return");
            String maxTotal = System.getProperty("redis.cluster.max.total");
            String maxIdle = System.getProperty("redis.cluster.max.idle");
            String minIdle = System.getProperty("redis.cluster.min.idle");
            String connectionTimeout = System.getProperty("redis.cluster.connection.timeout");
            String soTimeout = System.getProperty("redis.cluster.so.timeout");
            String maxAttempts = System.getProperty("redis.cluster.max.attempts");
            LOG.debug("Cluster Redis properties -> Host: {} Port: {} Password: {} Test On Borrow: {} Test On Return: {} Max Total: {} Max Idle: {} Min Idle: {} Connection Timeout: {} So Timeout: {} Max Attempts: {}", hosts, ports, password, testOnBorrow, testOnReturn, maxTotal, maxIdle, minIdle, connectionTimeout, soTimeout, maxAttempts);
            Set<HostAndPort> clusterRedisNodes = buildClusterRedisNodes(hosts, ports);
            GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
            genericObjectPoolConfig.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow));
            genericObjectPoolConfig.setTestOnReturn(Boolean.parseBoolean(testOnReturn));
            genericObjectPoolConfig.setMaxTotal(Integer.parseInt(maxTotal));
            genericObjectPoolConfig.setMaxIdle(Integer.parseInt(maxIdle));
            genericObjectPoolConfig.setMinIdle(Integer.parseInt(minIdle));
            JedisCluster jedisCluster;
            if (password == null || password.isEmpty()) {
                jedisCluster = new JedisCluster(clusterRedisNodes, Integer.parseInt(connectionTimeout), Integer.parseInt(soTimeout), Integer.parseInt(maxAttempts), genericObjectPoolConfig);
            } else {
                jedisCluster = new JedisCluster(clusterRedisNodes, Integer.parseInt(connectionTimeout), Integer.parseInt(soTimeout), Integer.parseInt(maxAttempts), password, genericObjectPoolConfig);
            }
            LOG.info("Cluster Redis were connected:");
            IntStream.range(0, hosts.size())
                    .forEach(i -> LOG.info("{}:{}", hosts.get(i), ports.get(i)));
            redisClusters.add(jedisCluster);
            return jedisCluster;
        } catch (Exception e) {
            throw new RedisConnectorRuntimeException(e, Error.REDIS_CLUSTER_CONNECTOR_ERROR);
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
        LOG.debug("Redis pools to close: {}", redisPools.size());
        redisPools.forEach(JedisPool::close);
        redisPools.clear();
    }

    @Override
    public void closeRedisClusters() {
        try {
            LOG.debug("Redis clusters to close: {}", redisClusters.size());
            for (JedisCluster redisCluster : redisClusters) {
                redisCluster.close();
            }
            redisClusters.clear();
        } catch (Exception e) {
            throw new CloseClusterRedisRuntimeException(e, Error.CLOSE_CLUSTER_REDIS_ERROR);
        }
    }

    @Override
    public void closeAll() {
        LOG.debug("Close all database connections");
        closeSyncMongoDBConnections();
        closeAsyncMongoDBConnections();
        closeRedisPools();
        closeRedisClusters();
    }

    private com.mongodb.client.MongoDatabase connectToStandaloneSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener) {
        String host = System.getProperty("sync.mongodb.standalone.host");
        String port = System.getProperty("sync.mongodb.standalone.port");
        String user = System.getProperty("sync.mongodb.standalone.user");
        String db = System.getProperty("sync.mongodb.standalone.db");
        String password = System.getProperty("sync.mongodb.standalone.pass");
        LOG.debug("Standalone sync MongoDB properties -> Host: {} Port: {} User: {} DB: {} Password: {}", host, port, user, db, password);
        ServerAddress serverAddress = new ServerAddress(host, Integer.parseInt(port));
        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(user, db, password.toCharArray());
        MongoClientOptions mongoClientOptions = buildSyncMongoClientOptions(commandListener, connectionPoolListener);
        com.mongodb.MongoClient mongoClient = new com.mongodb.MongoClient(serverAddress, Collections.singletonList(mongoCredential), mongoClientOptions);
        com.mongodb.client.MongoDatabase database = mongoClient.getDatabase(db);
        checkSyncMongoDBConnection(database);
        LOG.info("Standalone sync MongoDB {}:{} was connected", host, port);
        syncMongoClients.add(mongoClient);
        return database;
    }

    private com.mongodb.client.MongoDatabase connectToClusterSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener) {
        List<String> hosts = getSyncMongoDBListOfHosts();
        String port = System.getProperty("sync.mongodb.cluster.port");
        String user = System.getProperty("sync.mongodb.cluster.user");
        String db = System.getProperty("sync.mongodb.cluster.db");
        String password = System.getProperty("sync.mongodb.cluster.pass");
        LOG.debug("Cluster sync MongoDB properties -> Host: {} Port: {} User: {} DB: {} Password: {}", hosts, port, user, db, password);
        List<ServerAddress> serverAddresses = hosts.stream()
                .map(host -> new ServerAddress(host, Integer.parseInt(port)))
                .collect(Collectors.toList());
        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(user, db, password.toCharArray());
        MongoClientOptions mongoClientOptions = buildSyncMongoClientOptions(commandListener, connectionPoolListener);
        com.mongodb.MongoClient mongoClient = new com.mongodb.MongoClient(serverAddresses, Collections.singletonList(mongoCredential), mongoClientOptions);
        com.mongodb.client.MongoDatabase database = mongoClient.getDatabase(db);
        checkSyncMongoDBConnection(database);
        LOG.info("Cluster sync MongoDB were connected:");
        hosts.forEach(host -> LOG.info("{}:{}", host, port));
        syncMongoClients.add(mongoClient);
        return database;
    }

    private com.mongodb.async.client.MongoDatabase connectToStandaloneAsyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener) {
        String host = System.getProperty("async.mongodb.standalone.host");
        String port = System.getProperty("async.mongodb.standalone.port");
        String user = System.getProperty("async.mongodb.standalone.user");
        String db = System.getProperty("async.mongodb.standalone.db");
        String password = System.getProperty("async.mongodb.standalone.pass");
        LOG.debug("Standalone async MongoDB properties -> Host: {} Port: {} User: {} DB: {} Password: {}", host, port, user, db, password);
        ServerAddress serverAddress = new ServerAddress(host, Integer.parseInt(port));
        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(user, db, password.toCharArray());
        ClusterSettings clusterSettings = ClusterSettings.builder()
                .hosts(Collections.singletonList(serverAddress))
                .build();
        MongoClientSettings mongoClientSettings = buildAsyncMongoClientSettings(commandListener, connectionPoolListener, mongoCredential, clusterSettings);
        com.mongodb.async.client.MongoClient mongoClient = MongoClients.create(mongoClientSettings);
        com.mongodb.async.client.MongoDatabase database = mongoClient.getDatabase(db);
        checkAsyncMongoDBConnection(database);
        LOG.info("Standalone async MongoDB {}:{} was connected", host, port);
        asyncMongoClients.add(mongoClient);
        return database;
    }

    private com.mongodb.async.client.MongoDatabase connectToClusterAsyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener) {
        List<String> hosts = getAsyncMongoDBListOfHosts();
        String port = System.getProperty("async.mongodb.cluster.port");
        String user = System.getProperty("async.mongodb.cluster.user");
        String db = System.getProperty("async.mongodb.cluster.db");
        String password = System.getProperty("async.mongodb.cluster.pass");
        LOG.debug("Cluster async MongoDB properties -> Host: {} Port: {} User: {} DB: {} Password: {}", hosts, port, user, db, password);
        List<ServerAddress> serverAddresses = hosts.stream()
                .map(host -> new ServerAddress(host, Integer.parseInt(port)))
                .collect(Collectors.toList());
        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(user, db, password.toCharArray());
        ClusterSettings clusterSettings = ClusterSettings.builder()
                .hosts(serverAddresses)
                .build();
        MongoClientSettings mongoClientSettings = buildAsyncMongoClientSettings(commandListener, connectionPoolListener, mongoCredential, clusterSettings);
        com.mongodb.async.client.MongoClient mongoClient = MongoClients.create(mongoClientSettings);
        com.mongodb.async.client.MongoDatabase database = mongoClient.getDatabase(db);
        checkAsyncMongoDBConnection(database);
        LOG.info("Cluster async MongoDB were connected:");
        hosts.forEach(host -> LOG.info("{}:{}", host, port));
        asyncMongoClients.add(mongoClient);
        return database;
    }

    private MongoClientOptions buildSyncMongoClientOptions(CommandListener commandListener, ConnectionPoolListener connectionPoolListener) {
        MongoClientOptions.Builder mongoClientOptionsBuilder = MongoClientOptions.builder();
        if (commandListener != null) {
            mongoClientOptionsBuilder = mongoClientOptionsBuilder.addCommandListener(commandListener);
        }
        if (connectionPoolListener != null) {
            mongoClientOptionsBuilder = mongoClientOptionsBuilder.addConnectionPoolListener(connectionPoolListener);
        }
        return mongoClientOptionsBuilder.build();
    }

    private MongoClientSettings buildAsyncMongoClientSettings(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, MongoCredential mongoCredential, ClusterSettings clusterSettings) {
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
                new MongoShutdownServerCallback());
    }

    private void checkRedisPoolConnection(JedisPool redisPool) {
        try (Jedis jedis = redisPool.getResource()) {
            jedis.isConnected();
        }
    }
}
