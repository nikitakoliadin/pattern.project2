package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.exception.ValidationServiceRuntimeException;
import com.qthegamep.pattern.project2.model.container.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.stream.Collectors;

public class ValidationServiceImpl implements ValidationService {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationServiceImpl.class);

    private Validator validator;

    public ValidationServiceImpl() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public <T> void validate(T object) {
        if (object != null) {
            String error = validator.validate(object)
                    .stream()
                    .map(value -> String.format("'%s' %s", value.getPropertyPath(), value.getMessage()))
                    .collect(Collectors.joining("; "));
            LOG.debug("Validate error: {}", error);
            if (!error.isEmpty()) {
                throw new ValidationServiceRuntimeException(error, ErrorType.VALIDATION_ERROR);
            }
        }
    }
}
