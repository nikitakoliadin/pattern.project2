package com.qthegamep.pattern.project2.service;

import com.mongodb.event.CommandListener;
import com.mongodb.event.ConnectionPoolListener;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.List;

public interface DatabaseConnector {

    com.mongodb.client.MongoDatabase connectToSyncMongoDB();

    com.mongodb.client.MongoDatabase connectToSyncMongoDB(String connectionType);

    com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener);

    com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener, String connectionType);

    com.mongodb.client.MongoDatabase connectToSyncMongoDB(ConnectionPoolListener connectionPoolListener);

    com.mongodb.client.MongoDatabase connectToSyncMongoDB(ConnectionPoolListener connectionPoolListener, String connectionType);

    com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener);

    com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, String connectionType);

    List<com.mongodb.MongoClient> getSyncMongoDBConnections();

    void closeSyncMongoDBConnections();

    com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB();

    com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(String connectionType);

    com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener);

    com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener, String connectionType);

    com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(ConnectionPoolListener connectionPoolListener);

    com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(ConnectionPoolListener connectionPoolListener, String connectionType);

    com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener);

    com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, String connectionType);

    List<com.mongodb.async.client.MongoClient> getAsyncMongoDBConnections();

    void closeAsyncMongoDBConnections();

    JedisPool connectToPoolRedis();

    JedisCluster connectToClusterRedis();

    List<JedisPool> getRedisPools();

    List<JedisCluster> getRedisClusters();

    void closeRedisPools();

    void closeRedisClusters();

    void closeAll();
}
