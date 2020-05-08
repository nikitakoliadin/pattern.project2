package com.qthegamep.pattern.project2.repository.mongo;

import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import com.qthegamep.pattern.project2.binder.property.Property;
import com.qthegamep.pattern.project2.duration.Durationable;
import com.qthegamep.pattern.project2.repository.mongo.callback.AsyncInsertCallback;
import com.qthegamep.pattern.project2.exception.compile.MongoRepositoryAsyncException;
import com.qthegamep.pattern.project2.model.entity.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class MongoRepositoryAsyncImpl implements MongoRepositoryAsync {

    private static final Logger LOG = LoggerFactory.getLogger(MongoRepositoryAsyncImpl.class);

    private String errorCollectionName;
    private MongoDatabase mongoDatabase;

    @Inject
    public MongoRepositoryAsyncImpl(@Property(value = "mongodb.error.collection.name") String errorCollectionName,
                                    MongoDatabase mongoDatabase) {
        this.errorCollectionName = errorCollectionName;
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    @Durationable
    public void saveError(Error error) throws MongoRepositoryAsyncException {
        try {
            MongoCollection<Error> errorCollection = mongoDatabase.getCollection(errorCollectionName, Error.class);
            LOG.debug("Async save error: {} RequestId: {}", error, error.getRequestId());
            errorCollection.insertOne(error, new AsyncInsertCallback(error.getRequestId()));
        } catch (Exception e) {
            throw new MongoRepositoryAsyncException(e, com.qthegamep.pattern.project2.model.container.Error.MONGO_ASYNC_SAVE_ERROR_ERROR);
        }
    }
}
