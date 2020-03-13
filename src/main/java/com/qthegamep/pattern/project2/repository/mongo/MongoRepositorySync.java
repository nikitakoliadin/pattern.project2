package com.qthegamep.pattern.project2.repository.mongo;

import com.qthegamep.pattern.project2.model.entity.Error;
import com.qthegamep.pattern.project2.exception.compile.SyncMongoRepositoryException;

@FunctionalInterface
public interface MongoRepositorySync {

    void saveError(Error error) throws SyncMongoRepositoryException;
}
