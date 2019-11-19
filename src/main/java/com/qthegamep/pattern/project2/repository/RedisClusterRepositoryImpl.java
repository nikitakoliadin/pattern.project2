package com.qthegamep.pattern.project2.repository;

import com.qthegamep.pattern.project2.exception.RedisRepositoryException;
import com.qthegamep.pattern.project2.model.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

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

    @Override
    public void saveAll(String key, Map<String, String> value) throws RedisRepositoryException {
        saveAll(key, value, defaultTtl, null);
    }

    @Override
    public void saveAll(String key, Map<String, String> value, String requestId) throws RedisRepositoryException {
        saveAll(key, value, defaultTtl, requestId);
    }

    @Override
    public void saveAll(String key, Map<String, String> value, Integer ttl) throws RedisRepositoryException {
        saveAll(key, value, ttl, null);
    }

    @Override
    public void saveAll(String key, Map<String, String> value, Integer ttl, String requestId) throws RedisRepositoryException {
        LOG.debug("Key: {} Values: {} TTL: {} RequestId: {}", key, value, ttl, requestId);
        try {
            jedisCluster.hmset(key, value);
            jedisCluster.expire(key, ttl);
        } catch (Exception e) {
            throw new RedisRepositoryException(e, ErrorType.REDIS_SAVE_ALL_ERROR);
        }
    }

    @Override
    public Optional<String> read(String key) throws RedisRepositoryException {
        return read(key, null);
    }

    @Override
    public Optional<String> read(String key, String requestId) throws RedisRepositoryException {
        LOG.debug("Key: {} RequestId: {}", key, requestId);
        try {
            String result = jedisCluster.get(key);
            LOG.debug("Result: {} RequestId: {}", result, requestId);
            if (result == null || result.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(result);
            }
        } catch (Exception e) {
            throw new RedisRepositoryException(e, ErrorType.REDIS_READ_ERROR);
        }
    }

    @Override
    public Optional<Map<String, String>> readAll(String key) throws RedisRepositoryException {
        return readAll(key, null);
    }

    @Override
    public Optional<Map<String, String>> readAll(String key, String requestId) throws RedisRepositoryException {
        LOG.debug("Key: {} RequestId: {}", key, requestId);
        try {
            Map<String, String> result = jedisCluster.hgetAll(key);
            LOG.debug("Result: {} RequestId: {}", result, requestId);
            if (result == null || result.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(result);
            }
        } catch (Exception e) {
            throw new RedisRepositoryException(e, ErrorType.REDIS_READ_ALL_ERROR);
        }
    }
}
