package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.annotation.Cacheable;
import org.aspectj.lang.ProceedingJoinPoint;

@FunctionalInterface
public interface KeyBuilder {

    String buildCacheKey(ProceedingJoinPoint thisJoinPoint, Cacheable cacheable);
}
