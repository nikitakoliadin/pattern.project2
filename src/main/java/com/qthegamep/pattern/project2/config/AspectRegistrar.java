package com.qthegamep.pattern.project2.config;

import com.qthegamep.pattern.project2.aspect.CacheAspect;
import org.aspectj.lang.Aspects;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AspectRegistrar {

    private static final Logger LOG = LoggerFactory.getLogger(AspectRegistrar.class);

    private final boolean registerCacheAspect = Boolean.parseBoolean(System.getProperty("aop.register.cache.aspect"));

    public void register(ResourceConfig resourceConfig) {
        registerCacheAspect(resourceConfig);
    }

    private void registerCacheAspect(ResourceConfig resourceConfig) {
        LOG.debug("Register cache aspect: {}", registerCacheAspect);
        if (registerCacheAspect) {
            resourceConfig.register(Aspects.aspectOf(CacheAspect.class));
        }
    }
}
