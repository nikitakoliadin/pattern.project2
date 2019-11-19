package com.qthegamep.pattern.project2.repository;

import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import com.qthegamep.pattern.project2.callback.MongoAsyncInsertCallback;
import com.qthegamep.pattern.project2.entity.Error;
import com.qthegamep.pattern.project2.exception.AsyncMongoRepositoryException;
import com.qthegamep.pattern.project2.model.ErrorType;
import com.qthegamep.pattern.project2.service.ConverterService;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class AsyncMongoRepositoryImpl implements AsyncMongoRepository {

    private static final Logger LOG = LoggerFactory.getLogger(AsyncMongoRepositoryImpl.class);

    private final String errorCollectionName = System.getProperty("mongodb.error.collection.name");

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
            errorCollection.insertOne(query, new MongoAsyncInsertCallback(error.getRequestId()));
        } catch (Exception e) {
            throw new AsyncMongoRepositoryException(e, ErrorType.MONGO_ASYNC_SAVE_ERROR_ERROR);
        }
    }
}