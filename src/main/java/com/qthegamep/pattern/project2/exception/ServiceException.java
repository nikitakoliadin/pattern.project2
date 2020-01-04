package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.container.ErrorType;

public interface ServiceException {

    ErrorType getErrorType();
}
