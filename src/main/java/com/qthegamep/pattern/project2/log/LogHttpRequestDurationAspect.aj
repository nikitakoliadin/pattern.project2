package com.qthegamep.pattern.project2.log;

import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.MILLIS;

public aspect LogHttpRequestDurationAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LogHttpRequestDurationAspect.class);

    private static final boolean enableLogHttpRequestDurationAspect = Boolean.parseBoolean(System.getProperty("aop.enable.log.http.request.duration.aspect"));

    pointcut httpRequest(): (call(* javax.ws.rs.client.SyncInvoker.get(..)) || call(* javax.ws.rs.client.SyncInvoker.post(..))) && if(enableLogHttpRequestDurationAspect);

    public LogHttpRequestDurationAspect() {
        LOG.warn("Enable log http request duration aspect: {}", enableLogHttpRequestDurationAspect);
    }

    Object around(): httpRequest() {
        Signature signature = thisJoinPoint.getSignature();
        LocalDateTime startDateTime = LocalDateTime.now();
        Object result = proceed();
        LocalDateTime endDateTime = LocalDateTime.now();
        long duration = MILLIS.between(startDateTime, endDateTime);
        LOG.info("Http request method {} executed {} ms", signature, duration);
        return result;
    }
}
