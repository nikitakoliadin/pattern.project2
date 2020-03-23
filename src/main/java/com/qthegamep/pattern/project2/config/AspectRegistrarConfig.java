package com.qthegamep.pattern.project2.config;

import com.qthegamep.pattern.project2.cache.CacheAspect;
import org.aspectj.lang.Aspects;
import org.aspectj.lang.NoAspectBoundException;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AspectRegistrarConfig {

    private static final Logger LOG = LoggerFactory.getLogger(AspectRegistrarConfig.class);

    private final boolean registerCacheAspect = Boolean.parseBoolean(System.getProperty("aop.register.cache.aspect"));

    public void register(ResourceConfig resourceConfig) {
        try {
            registerCacheAspect(resourceConfig);
        } catch (NoAspectBoundException e) {
            LOG.warn("Aspects disabled!");
        }
    }

    private void registerCacheAspect(ResourceConfig resourceConfig) {
        LOG.debug("Register cache aspect: {}", registerCacheAspect);
        if (registerCacheAspect) {
            resourceConfig.register(Aspects.aspectOf(CacheAspect.class));
        }
    }
}
