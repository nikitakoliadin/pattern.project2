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
import com.qthegamep.pattern.project2.exception.AsyncMongoDBConnectorRuntimeException;
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

    private List<com.mongodb.MongoClient> syncMongoClients = new CopyOnWriteArrayList<>();
    private List<com.mongodb.async.client.MongoClient> asyncMongoClients = new CopyOnWriteArrayList<>();

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
                throw new SyncMongoDBConnectorRuntimeException(ErrorType.MONGO_DB_NOT_EXISTING_TYPE_ERROR);
            }
        } catch (SyncMongoDBConnectorRuntimeException e) {
            throw new SyncMongoDBConnectorRuntimeException(e, e.getErrorType());
        } catch (Exception e) {
            throw new SyncMongoDBConnectorRuntimeException(e, ErrorType.SYNC_MONGO_DB_CONNECTOR_ERROR);
        }
    }

    @Override
    public List<com.mongodb.MongoClient> getAllSyncMongoDBConnections() {
        return syncMongoClients;
    }

    @Override
    public void closeSyncConnections() {
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
                throw new AsyncMongoDBConnectorRuntimeException(ErrorType.MONGO_DB_NOT_EXISTING_TYPE_ERROR);
            }
        } catch (AsyncMongoDBConnectorRuntimeException e) {
            throw new AsyncMongoDBConnectorRuntimeException(e, e.getErrorType());
        } catch (Exception e) {
            throw new AsyncMongoDBConnectorRuntimeException(e, ErrorType.ASYNC_MONGO_DB_CONNECTOR_ERROR);
        }
    }

    @Override
    public List<com.mongodb.async.client.MongoClient> getAllAsyncMongoDBConnections() {
        return asyncMongoClients;
    }

    @Override
    public void closeAsyncConnections() {
        LOG.debug("Async MongoDB's to close: {}", asyncMongoClients.size());
        asyncMongoClients.forEach(com.mongodb.async.client.MongoClient::close);
        asyncMongoClients.clear();
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
        LOG.info("Standalone sync MongoDB {}:{} was connected", host, port);
        syncMongoClients.add(mongoClient);
        return mongoClient.getDatabase(db);
    }

    private com.mongodb.client.MongoDatabase connectToClusterSyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener) {
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
        com.mongodb.MongoClient mongoClient = new com.mongodb.MongoClient(serverAddresses, Collections.singletonList(mongoCredential), mongoClientOptions);
        LOG.info("Cluster sync MongoDB were connected:");
        hosts.forEach(host -> LOG.info("{}:{}", host, port));
        syncMongoClients.add(mongoClient);
        return mongoClient.getDatabase(db);
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
        LOG.info("Standalone async MongoDB {}:{} was connected", host, port);
        asyncMongoClients.add(mongoClient);
        return mongoClient.getDatabase(db);
    }

    private com.mongodb.async.client.MongoDatabase connectToClusterAsyncMongoDB(CommandListener commandListener, ConnectionPoolListener connectionPoolListener) {
        List<String> hosts = getAsyncListOfHosts();
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
        LOG.info("Cluster async MongoDB were connected:");
        hosts.forEach(host -> LOG.info("{}:{}", host, port));
        asyncMongoClients.add(mongoClient);
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

    private List<String> getSyncListOfHosts() {
        return getListOfHosts("sync.mongodb.cluster.host");
    }

    private List<String> getAsyncListOfHosts() {
        return getListOfHosts("async.mongodb.cluster.host");
    }

    private List<String> getListOfHosts(String hostProperty) {
        List<String> hosts = new ArrayList<>();
        int i = 1;
        while (true) {
            String host = System.getProperty(hostProperty + "." + i);
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
