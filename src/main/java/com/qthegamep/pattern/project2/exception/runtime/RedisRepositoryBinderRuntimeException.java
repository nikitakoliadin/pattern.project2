package com.qthegamep.pattern.project2.exception.runtime;

import com.qthegamep.pattern.project2.model.container.Error;

public class RedisRepositoryBinderRuntimeException extends GeneralServiceRuntimeException {

    public RedisRepositoryBinderRuntimeException(Error error) {
        super(error);
    }
}
