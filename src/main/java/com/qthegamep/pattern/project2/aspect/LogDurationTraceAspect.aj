package com.qthegamep.pattern.project2.aspect;

import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.*;

public aspect LogDurationTraceAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LogDurationTraceAspect.class);

    private final boolean enableLogDurationTraceAspect = LOG.isTraceEnabled();

    pointcut all(): execution(* *(..));

    public LogDurationTraceAspect() {
        LOG.warn("Enable log duration trace aspect: {}", enableLogDurationTraceAspect);
    }

    Object around(): all() {
        if (enableLogDurationTraceAspect) {
            Signature signature = thisJoinPoint.getSignature();
            Object[] args = thisJoinPoint.getArgs();
            LocalDateTime startDateTime = LocalDateTime.now();
            Object result = proceed();
            LocalDateTime endDateTime = LocalDateTime.now();
            long duration = MILLIS.between(startDateTime, endDateTime);
            LOG.trace("{} : {} : {} : {} ms", signature, args, result, duration);
            return result;
        } else {
            return proceed();
        }
    }
}
