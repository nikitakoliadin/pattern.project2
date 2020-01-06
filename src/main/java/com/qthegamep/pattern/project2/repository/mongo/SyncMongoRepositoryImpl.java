package com.qthegamep.pattern.project2.repository.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.qthegamep.pattern.project2.binder.property.Property;
import com.qthegamep.pattern.project2.exception.compile.SyncMongoRepositoryException;
import com.qthegamep.pattern.project2.model.entity.Error;
import com.qthegamep.pattern.project2.service.ConverterService;
import com.qthegamep.pattern.project2.util.Constants;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class SyncMongoRepositoryImpl implements SyncMongoRepository {

    private static final Logger LOG = LoggerFactory.getLogger(SyncMongoRepositoryImpl.class);

    @Property(value = "mongodb.error.collection.name")
    private String errorCollectionName;

    private MongoDatabase mongoDatabase;
    private ConverterService converterService;

    @Inject
    public SyncMongoRepositoryImpl(MongoDatabase mongoDatabase, ConverterService converterService) {
        this.mongoDatabase = mongoDatabase;
        this.converterService = converterService;
    }

    @Override
    public Error saveError(Error error) throws SyncMongoRepositoryException {
        try {
            MongoCollection<Document> errorCollection = mongoDatabase.getCollection(errorCollectionName);
            String errorJson = converterService.toJson(error);
            LOG.debug("Sync save error: {} RequestId: {}", errorJson, error.getRequestId());
            Document query = Document.parse(errorJson);
            errorCollection.insertOne(query);
            ObjectId objectId = query.getObjectId(Constants.JSON_OBJECT_ID_KEY);
            LOG.debug("ObjectId: {} RequestId: {}", objectId, error.getRequestId());
            error.setObjectId(objectId);
            return error;
        } catch (Exception e) {
            throw new SyncMongoRepositoryException(e, com.qthegamep.pattern.project2.model.container.Error.MONGO_SYNC_SAVE_ERROR_ERROR);
        }
    }
}
