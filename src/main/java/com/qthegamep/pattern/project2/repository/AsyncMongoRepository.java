package com.qthegamep.pattern.project2.repository;

import com.qthegamep.pattern.project2.model.entity.Error;
import com.qthegamep.pattern.project2.exception.AsyncMongoRepositoryException;

public interface AsyncMongoRepository {

    void saveError(Error error) throws AsyncMongoRepositoryException;
}
