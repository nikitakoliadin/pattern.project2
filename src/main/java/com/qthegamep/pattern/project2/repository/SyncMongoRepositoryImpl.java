package com.qthegamep.pattern.project2.repository;

import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class SyncMongoRepositoryImpl implements SyncMongoRepository {

    private static final Logger LOG = LoggerFactory.getLogger(SyncMongoRepositoryImpl.class);

    private MongoDatabase mongoDatabase;

    @Inject
    public SyncMongoRepositoryImpl(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }
}
