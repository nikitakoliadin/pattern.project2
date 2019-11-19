package com.qthegamep.pattern.project2.repository;

import com.qthegamep.pattern.project2.entity.Error;
import com.qthegamep.pattern.project2.exception.SyncMongoRepositoryException;

public interface SyncMongoRepository {

    Error saveError(Error error) throws SyncMongoRepositoryException;
}
