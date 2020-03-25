package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.model.container.KeyAlgorithm;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class KeyBuilderServiceImpl implements KeyBuilderService {

    private static final Logger LOG = LoggerFactory.getLogger(KeyBuilderServiceImpl.class);

    @Override
    public String buildCacheKey(ProceedingJoinPoint thisJoinPoint, KeyAlgorithm keyAlgorithm) {
        LOG.debug("Cache key algorithm: {}", keyAlgorithm);
        switch (keyAlgorithm) {
            case FULL_SIGNATURE_KEY_ALGORITHM:
                return keyWithFullSignature(thisJoinPoint);
            case FULL_ARGUMENTS_KEY_ALGORITHM:
                return keyWithFullArguments(thisJoinPoint);
            case FULL_SIGNATURE_WITH_FULL_ARGUMENTS_KEY_ALGORITHM:
            default:
                return keyWithFullSignatureWithFullArguments(thisJoinPoint);
        }
    }

    private String keyWithFullSignature(ProceedingJoinPoint thisJoinPoint) {
        Signature signature = thisJoinPoint.getSignature();
        return signature.toString();
    }

    private String keyWithFullArguments(ProceedingJoinPoint thisJoinPoint) {
        Object[] arguments = thisJoinPoint.getArgs();
        return Arrays.toString(arguments);
    }

    private String keyWithFullSignatureWithFullArguments(ProceedingJoinPoint thisJoinPoint) {
        Signature signature = thisJoinPoint.getSignature();
        Object[] arguments = thisJoinPoint.getArgs();
        return signature.toString() + Arrays.toString(arguments);
    }
}
