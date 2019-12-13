package com.qthegamep.pattern.project2.aspect;

import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.MILLIS;

public aspect LogRepositoryDurationAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LogRepositoryDurationAspect.class);

    private final boolean enableLogRepositoryDurationAspect = Boolean.parseBoolean(System.getProperty("aop.enable.log.repository.duration.aspect"));

    pointcut repository(): execution(* *..repository..*(..));

    public LogRepositoryDurationAspect() {
        LOG.warn("Enable log repository duration aspect: {}", enableLogRepositoryDurationAspect);
    }

    Object around(): repository() {
        if (enableLogRepositoryDurationAspect) {
            Signature signature = thisJoinPoint.getSignature();
            Object[] args = thisJoinPoint.getArgs();
            LocalDateTime startDateTime = LocalDateTime.now();
            Object result = proceed();
            LocalDateTime endDateTime = LocalDateTime.now();
            long duration = MILLIS.between(startDateTime, endDateTime);
            LOG.info("Repository method {} with arguments {} executed {} ms", signature, args, duration);
            return result;
        } else {
            return proceed();
        }
    }
}
