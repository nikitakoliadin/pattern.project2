package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.cache.Cacheable;
import org.aspectj.lang.ProceedingJoinPoint;

@FunctionalInterface
public interface KeyBuilderService {

    String buildCacheKey(ProceedingJoinPoint thisJoinPoint, Cacheable cacheable);
}
