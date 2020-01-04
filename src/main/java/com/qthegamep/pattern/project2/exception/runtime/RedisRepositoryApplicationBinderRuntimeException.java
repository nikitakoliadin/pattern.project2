package com.qthegamep.pattern.project2.exception.runtime;

import com.qthegamep.pattern.project2.model.container.Error;

public class RedisRepositoryApplicationBinderRuntimeException extends GeneralServiceRuntimeException {

    public RedisRepositoryApplicationBinderRuntimeException(Error error) {
        super(error);
    }
}
