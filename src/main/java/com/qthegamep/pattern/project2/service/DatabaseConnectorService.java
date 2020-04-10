package com.qthegamep.pattern.project2.service;

import com.mongodb.event.CommandListener;
import com.mongodb.event.ConnectionPoolListener;
import com.qthegamep.pattern.project2.model.container.MongoConnection;
import org.bson.codecs.configuration.CodecRegistry;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.List;

public interface DatabaseConnectorService {

    com.mongodb.client.MongoDatabase connectToSyncMongoDB();

    com.mongodb.client.MongoDatabase connectToSyncMongoDB(MongoConnection connectionType);

    com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener);

    com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener, MongoConnection connectionType);

    com.mongodb.client.MongoDatabase connectToSyncMongoDB(ConnectionPoolListener connectionPoolListener);

    com.mongodb.client.MongoDatabase connectToSyncMongoDB(ConnectionPoolListener connectionPoolListener, MongoConnection connectionType);

    com.mongodb.client.MongoDatabase connectToSyncMongoDB(CodecRegistry codecRegistry);

    com.mongodb.client.MongoDatabase connectToSyncMongoDB(CodecRegistry codecRegistry, MongoConnection connectionType);

    com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener);

    com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, MongoConnection connectionType);

    com.mongodb.client.MongoDatabase connectToSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, CodecRegistry codecRegistry, MongoConnection connectionType);

    com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB();

    com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(MongoConnection connectionType);

    com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener);

    com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener, MongoConnection connectionType);

    com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(ConnectionPoolListener connectionPoolListener);

    com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(ConnectionPoolListener connectionPoolListener, MongoConnection connectionType);

    com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CodecRegistry codecRegistry);

    com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CodecRegistry codecRegistry, MongoConnection connectionType);

    com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener);

    com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, MongoConnection connectionType);

    com.mongodb.async.client.MongoDatabase connectToAsyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, CodecRegistry codecRegistry, MongoConnection connectionType);

    JedisPool connectToPoolRedis();

    JedisCluster connectToClusterRedis();

    List<com.mongodb.MongoClient> getSyncMongoDBConnections();

    List<com.mongodb.async.client.MongoClient> getAsyncMongoDBConnections();

    List<JedisPool> getRedisPools();

    List<JedisCluster> getRedisClusters();

    void closeSyncMongoDBConnections();

    void closeAsyncMongoDBConnections();

    void closeRedisPools();

    void closeRedisClusters();

    void closeAll();
}
