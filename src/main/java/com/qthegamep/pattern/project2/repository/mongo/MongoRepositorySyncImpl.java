package com.qthegamep.pattern.project2.repository.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.qthegamep.pattern.project2.binder.property.Property;
import com.qthegamep.pattern.project2.duration.Durationable;
import com.qthegamep.pattern.project2.exception.compile.MongoRepositorySyncException;
import com.qthegamep.pattern.project2.model.entity.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class MongoRepositorySyncImpl implements MongoRepositorySync {

    private static final Logger LOG = LoggerFactory.getLogger(MongoRepositorySyncImpl.class);

    private String errorCollectionName;
    private MongoDatabase mongoDatabase;

    @Inject
    public MongoRepositorySyncImpl(@Property(value = "mongodb.error.collection.name") String errorCollectionName,
                                   MongoDatabase mongoDatabase) {
        this.errorCollectionName = errorCollectionName;
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    @Durationable
    public void saveError(Error error) throws MongoRepositorySyncException {
        try {
            MongoCollection<Error> errorCollection = mongoDatabase.getCollection(errorCollectionName, Error.class);
            LOG.debug("Sync save error: {} RequestId: {}", error, error.getRequestId());
            errorCollection.insertOne(error);
        } catch (Exception e) {
            throw new MongoRepositorySyncException(e, com.qthegamep.pattern.project2.model.container.Error.MONGO_SYNC_SAVE_ERROR_ERROR);
        }
    }
}
