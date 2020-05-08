package com.qthegamep.pattern.project2.duration;

import com.qthegamep.pattern.project2.util.Constants;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.MILLIS;

@Aspect
public class DurationAspect {

    private static final Logger LOG = LoggerFactory.getLogger(DurationAspect.class);

    private final boolean enableDurationAspect = Boolean.parseBoolean(System.getProperty("aop.enable.duration.aspect"));

    @Inject
    private HttpHeaders httpHeaders;

    public DurationAspect() {
        LOG.warn("Enable duration aspect: {}", enableDurationAspect);
    }

    @Pointcut("@annotation(durationable)")
    void durationableAnnotation(Durationable durationable) {
    }

    @Around(value = "durationableAnnotation(durationable)",
            argNames = "thisJoinPoint,durationable")
    public Object duration(ProceedingJoinPoint thisJoinPoint, Durationable durationable) throws Throwable {
        if (enableDurationAspect) {
            String requestId = getRequestId();
            Signature signature = thisJoinPoint.getSignature();
            LocalDateTime startDateTime = LocalDateTime.now();
            Object result = thisJoinPoint.proceed();
            LocalDateTime endDateTime = LocalDateTime.now();
            long duration = MILLIS.between(startDateTime, endDateTime);
            LOG.info("Method {} executed {} ms RequestId: {}", signature, duration, requestId);
            return result;
        } else {
            return thisJoinPoint.proceed();
        }
    }

    private String getRequestId() {
        if (httpHeaders != null) {
            return httpHeaders.getHeaderString(Constants.REQUEST_ID_HEADER);
        } else {
            return null;
        }
    }
}
