package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.model.container.KeyAlgorithm;
import org.aspectj.lang.ProceedingJoinPoint;

@FunctionalInterface
public interface KeyBuilderService {

    String buildCacheKey(ProceedingJoinPoint thisJoinPoint, KeyAlgorithm keyAlgorithm);
}
