package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.container.Error;

public class RedisBinderRuntimeException extends GeneralServiceRuntimeException {

    public RedisBinderRuntimeException(Error error) {
        super(error);
    }
}
