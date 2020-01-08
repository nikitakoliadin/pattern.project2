package com.qthegamep.pattern.project2.repository.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.qthegamep.pattern.project2.binder.property.Property;
import com.qthegamep.pattern.project2.exception.compile.SyncMongoRepositoryException;
import com.qthegamep.pattern.project2.model.entity.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class SyncMongoRepositoryImpl implements SyncMongoRepository {

    private static final Logger LOG = LoggerFactory.getLogger(SyncMongoRepositoryImpl.class);

    @Property(value = "mongodb.error.collection.name")
    private String errorCollectionName;

    private MongoDatabase mongoDatabase;

    @Inject
    public SyncMongoRepositoryImpl(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public void saveError(Error error) throws SyncMongoRepositoryException {
        try {
            MongoCollection<Error> errorCollection = mongoDatabase.getCollection(errorCollectionName, Error.class);
            LOG.debug("Sync save error: {} RequestId: {}", error, error.getRequestId());
            errorCollection.insertOne(error);
        } catch (Exception e) {
            throw new SyncMongoRepositoryException(e, com.qthegamep.pattern.project2.model.container.Error.MONGO_SYNC_SAVE_ERROR_ERROR);
        }
    }
}
