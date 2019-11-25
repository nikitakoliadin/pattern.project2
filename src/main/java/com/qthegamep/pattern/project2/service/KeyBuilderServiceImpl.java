package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.annotation.Cacheable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class KeyBuilderServiceImpl implements KeyBuilderService {

    private static final Logger LOG = LoggerFactory.getLogger(KeyBuilderServiceImpl.class);

    @Override
    public String buildCacheKey(ProceedingJoinPoint thisJoinPoint, Cacheable cacheable) {
        LOG.debug("Cache key algorithm: {}", cacheable.keyAlgorithm());
        switch (cacheable.keyAlgorithm()) {
            case FULL_SIGNATURE: {
                Signature signature = thisJoinPoint.getSignature();
                return signature.toString();
            }
            case FULL_ARGUMENTS: {
                Object[] arguments = thisJoinPoint.getArgs();
                return Arrays.toString(arguments);
            }
            case FULL_SIGNATURE_WITH_FULL_ARGUMENTS:
            default: {
                Signature signature = thisJoinPoint.getSignature();
                Object[] arguments = thisJoinPoint.getArgs();
                return signature.toString() + Arrays.toString(arguments);
            }
        }
    }
}
