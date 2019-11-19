package com.qthegamep.pattern.project2.repository;

import com.mongodb.async.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class AsyncMongoRepositoryImpl implements AsyncMongoRepository {

    private static final Logger LOG = LoggerFactory.getLogger(AsyncMongoRepositoryImpl.class);

    private MongoDatabase mongoDatabase;

    @Inject
    public AsyncMongoRepositoryImpl(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }
}
