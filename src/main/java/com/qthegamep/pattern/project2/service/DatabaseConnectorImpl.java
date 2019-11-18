package com.qthegamep.pattern.project2.service;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import com.mongodb.event.CommandListener;
import com.mongodb.event.ConnectionPoolListener;
import com.qthegamep.pattern.project2.exception.SyncMongoDBConnectorRuntimeException;
import com.qthegamep.pattern.project2.model.ErrorType;
import com.qthegamep.pattern.project2.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class DatabaseConnectorImpl implements DatabaseConnector {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseConnectorImpl.class);

    private List<MongoClient> syncMongoClients = new CopyOnWriteArrayList<>();

    @Override
    public MongoDatabase connectToSyncMongoDB() {
        return connectToSyncMongoDB(null, null, Constants.STANDALONE_MONGO_DB_TYPE.getValue());
    }

    @Override
    public MongoDatabase connectToSyncMongoDB(String connectionType) {
        return connectToSyncMongoDB(null, null, connectionType);
    }

    @Override
    public MongoDatabase connectToSyncMongoDB(CommandListener commandListener) {
        return connectToSyncMongoDB(commandListener, null, Constants.STANDALONE_MONGO_DB_TYPE.getValue());
    }

    @Override
    public MongoDatabase connectToSyncMongoDB(CommandListener commandListener, String connectionType) {
        return connectToSyncMongoDB(commandListener, null, connectionType);
    }

    @Override
    public MongoDatabase connectToSyncMongoDB(ConnectionPoolListener connectionPoolListener) {
        return connectToSyncMongoDB(null, connectionPoolListener, Constants.STANDALONE_MONGO_DB_TYPE.getValue());
    }

    @Override
    public MongoDatabase connectToSyncMongoDB(ConnectionPoolListener connectionPoolListener, String connectionType) {
        return connectToSyncMongoDB(null, connectionPoolListener, connectionType);
    }

    @Override
    public MongoDatabase connectToSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener) {
        return connectToSyncMongoDB(commandListener, connectionPoolListener, Constants.STANDALONE_MONGO_DB_TYPE.getValue());
    }

    @Override
    public MongoDatabase connectToSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener, String connectionType) {
        LOG.debug("Sync MongoDB type: {}", connectionType);
        try {
            if (Constants.STANDALONE_MONGO_DB_TYPE.getValue().equalsIgnoreCase(connectionType)) {
                return connectToStandaloneSyncMongoDB(commandListener, connectionPoolListener);
            } else if (Constants.CLUSTER_MONGO_DB_TYPE.getValue().equalsIgnoreCase(connectionType)) {
                return connectToClusterSyncMongoDB(commandListener, connectionPoolListener);
            } else {
                throw new SyncMongoDBConnectorRuntimeException(ErrorType.MONGO_DB_NOT_EXISTING_TYPE_ERROR);
            }
        } catch (SyncMongoDBConnectorRuntimeException e) {
            throw new SyncMongoDBConnectorRuntimeException(e, e.getErrorType());
        } catch (Exception e) {
            throw new SyncMongoDBConnectorRuntimeException(e, ErrorType.SYNC_MONGO_DB_CONNECTOR_ERROR);
        }
    }

    @Override
    public List<MongoClient> getAllSyncMongoDBConnections() {
        return syncMongoClients;
    }

    @Override
    public void closeSyncConnections() {
        LOG.debug("Sync MongoDB's to close: {}", syncMongoClients.size());
        syncMongoClients.forEach(Mongo::close);
        syncMongoClients.clear();
    }

    private MongoDatabase connectToStandaloneSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener) {
        String host = System.getProperty("sync.mongodb.standalone.host");
        String port = System.getProperty("sync.mongodb.standalone.port");
        String user = System.getProperty("sync.mongodb.standalone.user");
        String db = System.getProperty("sync.mongodb.standalone.db");
        String password = System.getProperty("sync.mongodb.standalone.pass");
        LOG.debug("Standalone sync MongoDB properties -> Host: {} Port: {} User: {} DB: {} Password: {}", host, port, user, db, password);
        ServerAddress serverAddress = new ServerAddress(host, Integer.parseInt(port));
        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(user, db, password.toCharArray());
        MongoClientOptions mongoClientOptions = buildSyncMongoClientOptions(commandListener, connectionPoolListener);
        MongoClient mongoClient = new MongoClient(serverAddress, Collections.singletonList(mongoCredential), mongoClientOptions);
        LOG.info("Standalone sync MongoDB {}:{} was connected", host, port);
        syncMongoClients.add(mongoClient);
        return mongoClient.getDatabase(db);
    }

    private MongoDatabase connectToClusterSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener) {
        List<String> hosts = getSyncListOfHosts();
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
        MongoClient mongoClient = new MongoClient(serverAddresses, Collections.singletonList(mongoCredential), mongoClientOptions);
        LOG.info("Cluster sync MongoDB were connected:");
        hosts.forEach(host -> LOG.info("{}:{}", host, port));
        syncMongoClients.add(mongoClient);
        return mongoClient.getDatabase(db);
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

    private List<String> getSyncListOfHosts() {
        List<String> hosts = new ArrayList<>();
        int i = 1;
        while (true) {
            String host = System.getProperty("sync.mongodb.cluster.host." + i);
            if (host == null || host.isEmpty()) {
                break;
            } else {
                hosts.add(host);
                i++;
            }
        }
        return hosts;
    }
}
