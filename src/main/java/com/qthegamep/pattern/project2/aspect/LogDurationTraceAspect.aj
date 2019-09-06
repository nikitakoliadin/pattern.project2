package com.qthegamep.pattern.project2.aspect;

import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.*;

public aspect LogDurationTraceAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LogDurationTraceAspect.class);

    pointcut all(): execution(* *(..));

    Object around(): all() {
        Signature signature = thisJoinPoint.getSignature();
        LocalDateTime startDateTime = LocalDateTime.now();
        Object result = proceed();
        LocalDateTime endDateTime = LocalDateTime.now();
        long duration = MILLIS.between(startDateTime, endDateTime);
        LOG.trace("{} : {} ms", signature, duration);
        return result;
    }
}
