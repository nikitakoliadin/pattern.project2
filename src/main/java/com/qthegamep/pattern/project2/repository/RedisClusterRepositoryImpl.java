package com.qthegamep.pattern.project2.repository;

import com.qthegamep.pattern.project2.annotation.Property;
import com.qthegamep.pattern.project2.exception.RedisRepositoryException;
import com.qthegamep.pattern.project2.metric.Metrics;
import com.qthegamep.pattern.project2.model.container.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

public class RedisClusterRepositoryImpl implements RedisRepository {

    private static final Logger LOG = LoggerFactory.getLogger(RedisClusterRepositoryImpl.class);

    @Property(value = "redis.cluster.default.ttl")
    private Integer defaultTtl;
    @Property(value = "redis.cluster.fall.when.error")
    private Boolean fallWhenError;

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
        LOG.debug("Save -> Key: {} Value: {} TTL: {} RequestId: {}", key, value, ttl, requestId);
        try {
            jedisCluster.set(key, value);
            jedisCluster.expire(key, ttl);
        } catch (Exception e) {
            Metrics.REDIS_ERROR_COUNTER_METRIC.incrementAndGet();
            if (fallWhenError) {
                throw new RedisRepositoryException(e, ErrorType.REDIS_SAVE_ERROR);
            } else {
                LOG.error("Error when save to Redis. Key: {} Value: {} TTL: {} RequestId: {}", key, value, ttl, requestId, e);
            }
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
        LOG.debug("Save all -> Key: {} Values: {} TTL: {} RequestId: {}", key, value, ttl, requestId);
        try {
            jedisCluster.hmset(key, value);
            jedisCluster.expire(key, ttl);
        } catch (Exception e) {
            Metrics.REDIS_ERROR_COUNTER_METRIC.incrementAndGet();
            if (fallWhenError) {
                throw new RedisRepositoryException(e, ErrorType.REDIS_SAVE_ALL_ERROR);
            } else {
                LOG.error("Error when save all to Redis. Key: {} Values: {}, TTL: {} RequestId: {}", key, value, ttl, requestId, e);
            }
        }
    }

    @Override
    public Optional<String> read(String key) throws RedisRepositoryException {
        return read(key, null);
    }

    @Override
    public Optional<String> read(String key, String requestId) throws RedisRepositoryException {
        LOG.debug("Read -> Key: {} RequestId: {}", key, requestId);
        try {
            String result = jedisCluster.get(key);
            LOG.debug("Read result: {} RequestId: {}", result, requestId);
            if (result == null || result.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(result);
            }
        } catch (Exception e) {
            Metrics.REDIS_ERROR_COUNTER_METRIC.incrementAndGet();
            if (fallWhenError) {
                throw new RedisRepositoryException(e, ErrorType.REDIS_READ_ERROR);
            } else {
                LOG.error("Error when read from Redis. Key: {} RequestId: {}", key, requestId, e);
                return Optional.empty();
            }
        }
    }

    @Override
    public Optional<Map<String, String>> readAll(String key) throws RedisRepositoryException {
        return readAll(key, null);
    }

    @Override
    public Optional<Map<String, String>> readAll(String key, String requestId) throws RedisRepositoryException {
        LOG.debug("Read all -> Key: {} RequestId: {}", key, requestId);
        try {
            Map<String, String> result = jedisCluster.hgetAll(key);
            LOG.debug("Read all result: {} RequestId: {}", result, requestId);
            if (result == null || result.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(result);
            }
        } catch (Exception e) {
            Metrics.REDIS_ERROR_COUNTER_METRIC.incrementAndGet();
            if (fallWhenError) {
                throw new RedisRepositoryException(e, ErrorType.REDIS_READ_ALL_ERROR);
            } else {
                LOG.error("Error when read all from Redis. Key: {} RequestId: {}", key, requestId, e);
                return Optional.empty();
            }
        }
    }
}
