package com.qthegamep.pattern.project2.aspect;

import com.qthegamep.pattern.project2.annotation.Cacheable;
import com.qthegamep.pattern.project2.exception.compile.CryptoServiceException;
import com.qthegamep.pattern.project2.repository.redis.RedisRepository;
import com.qthegamep.pattern.project2.service.ConverterService;
import com.qthegamep.pattern.project2.service.CryptoService;
import com.qthegamep.pattern.project2.service.KeyBuilderService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.Optional;

@Aspect
public class CacheAspect {

    private static final Logger LOG = LoggerFactory.getLogger(CacheAspect.class);

    private final boolean enableCacheAspect = Boolean.parseBoolean(System.getProperty("aop.enable.cache.aspect"));

    @Inject
    private CryptoService cryptoService;
    @Inject
    private ConverterService converterService;
    @Inject
    private RedisRepository redisRepository;
    @Inject
    private KeyBuilderService keyBuilderService;

    public CacheAspect() {
        LOG.warn("Enable cache aspect: {}", enableCacheAspect);
    }

    @Pointcut("@annotation(cacheable)")
    void cacheableAnnotation(Cacheable cacheable) {
    }

    @Around(value = "cacheableAnnotation(cacheable)",
            argNames = "thisJoinPoint,cacheable")
    public Object cache(ProceedingJoinPoint thisJoinPoint, Cacheable cacheable) {
        try {
            if (enableCacheAspect) {
                LOG.debug("Cache method {} with arguments {} by {} hash algorithm", thisJoinPoint.getSignature(), thisJoinPoint.getArgs(), cacheable.hashAlgorithm());
                String key = getKey(thisJoinPoint, cacheable);
                Optional<String> resultFromCache = redisRepository.read(key);
                LOG.debug("Has result: {} by key: {}", resultFromCache.isPresent(), key);
                if (resultFromCache.isPresent()) {
                    String result = resultFromCache.get();
                    LOG.info("Result from cache: {} by key: {}", result, key);
                    Class<?> returnType = getReturnType(thisJoinPoint);
                    return converterService.fromJson(result, returnType);
                } else {
                    Object result = thisJoinPoint.proceed();
                    LOG.debug("Result from proceed: {}", result);
                    String jsonResult = converterService.toJson(result);
                    redisRepository.save(key, jsonResult);
                    return result;
                }
            } else {
                return thisJoinPoint.proceed();
            }
        } catch (Throwable e) {
            LOG.error("ERROR", e);
            return e;
        }
    }

    private String getKey(ProceedingJoinPoint thisJoinPoint, Cacheable cacheable) throws CryptoServiceException {
        String key = keyBuilderService.buildCacheKey(thisJoinPoint, cacheable);
        String encodedKey = cryptoService.encodeTo(key, cacheable.hashAlgorithm());
        LOG.debug("Encoded key: {}", encodedKey);
        return encodedKey;
    }

    private Class<?> getReturnType(ProceedingJoinPoint thisJoinPoint) {
        MethodSignature methodSignature = (MethodSignature) thisJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method.getReturnType();
    }
}
