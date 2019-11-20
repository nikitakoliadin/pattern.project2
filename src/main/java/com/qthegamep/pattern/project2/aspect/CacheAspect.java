package com.qthegamep.pattern.project2.aspect;

import com.qthegamep.pattern.project2.annotation.Cacheable;
import com.qthegamep.pattern.project2.binder.ApplicationBinder;
import com.qthegamep.pattern.project2.exception.CryptoServiceException;
import com.qthegamep.pattern.project2.repository.RedisRepository;
import com.qthegamep.pattern.project2.service.ConverterService;
import com.qthegamep.pattern.project2.service.CryptoService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.glassfish.jersey.internal.inject.Injections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

@Aspect
public class CacheAspect {

    private static final Logger LOG = LoggerFactory.getLogger(CacheAspect.class);

    private CryptoService cryptoService;
    private ConverterService converterService;
    private RedisRepository redisRepository;

    public CacheAspect() {
        // TODO: FIX TO CORRECT INJECT
        ApplicationBinder applicationBinder = ApplicationBinder.builder().build();
        InjectionManager injectionManager = Injections.createInjectionManager();
        injectionManager.register(applicationBinder);
        cryptoService = injectionManager.getInstance(CryptoService.class);
        converterService = injectionManager.getInstance(ConverterService.class);
        redisRepository = injectionManager.getInstance(RedisRepository.class);
    }

    @Pointcut("@annotation(cacheable)")
    void cacheableAnnotation(Cacheable cacheable) {
    }

    @Around(value = "cacheableAnnotation(cacheable)",
            argNames = "thisJoinPoint,cacheable")
    public Object cache(ProceedingJoinPoint thisJoinPoint, Cacheable cacheable) {
        try {
            LOG.debug("Cache method {} with arguments {} by {} hash algorithm", thisJoinPoint.getSignature(), thisJoinPoint.getArgs(), cacheable.hashAlgorithm());
            String key = getKey(thisJoinPoint, cacheable);
            Optional<String> resultFromCache = redisRepository.read(key);
            LOG.debug("Has result: {} by key: {}", resultFromCache.isPresent(), key);
            if (resultFromCache.isPresent()) {
                String result = resultFromCache.get();
                LOG.debug("Result from cache: {} by key: {}", result, key);
                Class<?> returnType = getReturnType(thisJoinPoint);
                return converterService.fromJson(result, returnType);
            } else {
                Object result = thisJoinPoint.proceed();
                LOG.debug("Result from proceed: {}", result);
                String jsonResult = converterService.toJson(result);
                redisRepository.save(key, jsonResult);
                return result;
            }
        } catch (Throwable e) {
            LOG.error("ERROR", e);
            return e;
        }
    }

    private String getKey(ProceedingJoinPoint thisJoinPoint, Cacheable cacheable) throws CryptoServiceException {
        Signature signature = thisJoinPoint.getSignature();
        Object[] args = thisJoinPoint.getArgs();
        String dataForKey = signature.toString() + Arrays.toString(args);
        String key = cryptoService.encodeTo(dataForKey, cacheable.hashAlgorithm());
        LOG.debug("Key: {}", key);
        return key;
    }

    private Class<?> getReturnType(ProceedingJoinPoint thisJoinPoint) {
        MethodSignature methodSignature = (MethodSignature) thisJoinPoint.getSignature();
        final Method method = methodSignature.getMethod();
        return method.getReturnType();
    }
}
