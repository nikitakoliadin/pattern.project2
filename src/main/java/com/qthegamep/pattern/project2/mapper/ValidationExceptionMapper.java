package com.qthegamep.pattern.project2.mapper;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private GeneralExceptionMapper generalExceptionMapper;

    @Inject
    public ValidationExceptionMapper(GeneralExceptionMapper generalExceptionMapper) {
        this.generalExceptionMapper = generalExceptionMapper;
    }

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        return generalExceptionMapper.toResponse(exception);
    }
}
