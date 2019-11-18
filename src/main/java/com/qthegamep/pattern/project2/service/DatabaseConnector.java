package com.qthegamep.pattern.project2.service;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.event.CommandListener;
import com.mongodb.event.ConnectionPoolListener;

import java.util.List;

public interface DatabaseConnector {

    MongoDatabase connectToSyncMongoDB();

    MongoDatabase connectToSyncMongoDB(String connectionType);

    MongoDatabase connectToSyncMongoDB(CommandListener commandListener);

    MongoDatabase connectToSyncMongoDB(CommandListener commandListener, String connectionType);

    MongoDatabase connectToSyncMongoDB(ConnectionPoolListener connectionPoolListener);

    MongoDatabase connectToSyncMongoDB(ConnectionPoolListener connectionPoolListener, String connectionType);

    MongoDatabase connectToSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener);

    MongoDatabase connectToSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, String connectionType);

    List<MongoClient> getAllSyncMongoDBConnections();

    void closeSyncConnections();
}
