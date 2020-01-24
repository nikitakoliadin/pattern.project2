package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.container.Error;

@FunctionalInterface
public interface ServiceException {

    Error getError();
}
