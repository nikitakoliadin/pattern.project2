package com.qthegamep.pattern.project2.repository.mongo;

import com.qthegamep.pattern.project2.model.entity.Error;
import com.qthegamep.pattern.project2.exception.compile.AsyncMongoRepositoryException;

@FunctionalInterface
public interface AsyncMongoRepository {

    void saveError(Error error) throws AsyncMongoRepositoryException;
}
