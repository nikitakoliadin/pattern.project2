package com.qthegamep.pattern.project2.repository;

import com.qthegamep.pattern.project2.exception.RedisRepositoryException;
import com.qthegamep.pattern.project2.model.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

import javax.inject.Inject;

public class RedisClusterRepositoryImpl implements RedisRepository {

    private static final Logger LOG = LoggerFactory.getLogger(RedisClusterRepositoryImpl.class);

    private final int defaultTtl = 1200;

    private JedisCluster jedisCluster;

    @Inject
    public RedisClusterRepositoryImpl(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    @Override
    public void save(String key, String value) throws RedisRepositoryException {
        save(key, value, defaultTtl, null);
    }

    @Override
    public void save(String key, String value, String requestId) throws RedisRepositoryException {
        save(key, value, defaultTtl, requestId);
    }

    @Override
    public void save(String key, String value, Integer ttl) throws RedisRepositoryException {
        save(key, value, ttl, null);
    }

    @Override
    public void save(String key, String value, Integer ttl, String requestId) throws RedisRepositoryException {
        LOG.debug("Key: {} Value: {} TTL: {} RequestId: {}", key, value, ttl, requestId);
        try {
            jedisCluster.set(key, value);
            jedisCluster.expire(key, ttl);
        } catch (Exception e) {
            throw new RedisRepositoryException(e, ErrorType.REDIS_SAVE_ERROR);
        }
    }
}
