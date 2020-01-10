package com.qthegamep.pattern.project2.exception.runtime;

import com.qthegamep.pattern.project2.model.container.Error;

public class RetryRuntimeException extends GeneralServiceRuntimeException {

    public RetryRuntimeException(Error error) {
        super(error);
    }

    public RetryRuntimeException(Throwable cause, Error error) {
        super(cause, error);
    }
}
