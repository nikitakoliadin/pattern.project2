package com.qthegamep.pattern.project2.repository.mongo;

import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import com.qthegamep.pattern.project2.binder.property.Property;
import com.qthegamep.pattern.project2.repository.mongo.callback.AsyncInsertCallback;
import com.qthegamep.pattern.project2.exception.compile.AsyncMongoRepositoryException;
import com.qthegamep.pattern.project2.model.entity.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class AsyncMongoRepositoryImpl implements AsyncMongoRepository {

    private static final Logger LOG = LoggerFactory.getLogger(AsyncMongoRepositoryImpl.class);

    @Property(value = "mongodb.error.collection.name")
    private String errorCollectionName;

    private MongoDatabase mongoDatabase;

    @Inject
    public AsyncMongoRepositoryImpl(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public void saveError(Error error) throws AsyncMongoRepositoryException {
        try {
            MongoCollection<Error> errorCollection = mongoDatabase.getCollection(errorCollectionName, Error.class);
            LOG.debug("Async save error: {} RequestId: {}", error, error.getRequestId());
            errorCollection.insertOne(error, new AsyncInsertCallback(error.getRequestId()));
        } catch (Exception e) {
            throw new AsyncMongoRepositoryException(e, com.qthegamep.pattern.project2.model.container.Error.MONGO_ASYNC_SAVE_ERROR_ERROR);
        }
    }
}
