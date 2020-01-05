package com.qthegamep.pattern.project2.repository.mongo;

import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import com.qthegamep.pattern.project2.annotation.Property;
import com.qthegamep.pattern.project2.repository.mongo.callback.AsyncInsertCallback;
import com.qthegamep.pattern.project2.exception.compile.AsyncMongoRepositoryException;
import com.qthegamep.pattern.project2.model.entity.Error;
import com.qthegamep.pattern.project2.service.ConverterService;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class AsyncMongoRepositoryImpl implements AsyncMongoRepository {

    private static final Logger LOG = LoggerFactory.getLogger(AsyncMongoRepositoryImpl.class);

    @Property(value = "mongodb.error.collection.name")
    private String errorCollectionName;

    private MongoDatabase mongoDatabase;
    private ConverterService converterService;

    @Inject
    public AsyncMongoRepositoryImpl(MongoDatabase mongoDatabase, ConverterService converterService) {
        this.mongoDatabase = mongoDatabase;
        this.converterService = converterService;
    }

    @Override
    public void saveError(Error error) throws AsyncMongoRepositoryException {
        try {
            MongoCollection<Document> errorCollection = mongoDatabase.getCollection(errorCollectionName);
            String errorJson = converterService.toJson(error);
            LOG.debug("Async save error: {} RequestId: {}", errorJson, error.getRequestId());
            Document query = Document.parse(errorJson);
            errorCollection.insertOne(query, new AsyncInsertCallback(error.getRequestId()));
        } catch (Exception e) {
            throw new AsyncMongoRepositoryException(e, com.qthegamep.pattern.project2.model.container.Error.MONGO_ASYNC_SAVE_ERROR_ERROR);
        }
    }
}