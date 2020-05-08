package com.qthegamep.pattern.project2.repository.redis;

import com.qthegamep.pattern.project2.binder.property.Property;
import com.qthegamep.pattern.project2.duration.Durationable;
import com.qthegamep.pattern.project2.exception.compile.RedisRepositoryException;
import com.qthegamep.pattern.project2.statistics.Meters;
import com.qthegamep.pattern.project2.model.container.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

public class RedisRepositoryClusterImpl implements RedisRepository {

    private static final Logger LOG = LoggerFactory.getLogger(RedisRepositoryClusterImpl.class);

    private Boolean shouldFallWhenError;
    private Integer defaultTtl;
    private JedisCluster jedisCluster;

    @Inject
    public RedisRepositoryClusterImpl(@Property(value = "redis.cluster.should.fall.when.error") Boolean shouldFallWhenError,
                                      @Property(value = "redis.cluster.default.ttl") Integer defaultTtl,
                                      JedisCluster jedisCluster) {
        this.shouldFallWhenError = shouldFallWhenError;
        this.defaultTtl = defaultTtl;
        this.jedisCluster = jedisCluster;
    }

    @Override
    @Durationable
    public void save(String key, String value) throws RedisRepositoryException {
        save(key, value, defaultTtl, null);
    }

    @Override
    @Durationable
    public void save(String key, String value, String requestId) throws RedisRepositoryException {
        save(key, value, defaultTtl, requestId);
    }

    @Override
    @Durationable
    public void save(String key, String value, Integer ttl) throws RedisRepositoryException {
        save(key, value, ttl, null);
    }

    @Override
    @Durationable
    public void save(String key, String value, Integer ttl, String requestId) throws RedisRepositoryException {
        LOG.debug("Save -> Key: {} Value: {} TTL: {} RequestId: {}", key, value, ttl, requestId);
        try {
            jedisCluster.set(key, value);
            jedisCluster.expire(key, ttl);
        } catch (Exception e) {
            Meters.REDIS_ERROR_COUNTER_METER.incrementAndGet();
            if (shouldFallWhenError) {
                throw new RedisRepositoryException(e, Error.REDIS_SAVE_ERROR);
            } else {
                LOG.error("Error when save to Redis. Key: {} Value: {} TTL: {} RequestId: {}", key, value, ttl, requestId, e);
            }
        }
    }

    @Override
    @Durationable
    public void saveAll(String key, Map<String, String> value) throws RedisRepositoryException {
        saveAll(key, value, defaultTtl, null);
    }

    @Override
    @Durationable
    public void saveAll(String key, Map<String, String> value, String requestId) throws RedisRepositoryException {
        saveAll(key, value, defaultTtl, requestId);
    }

    @Override
    @Durationable
    public void saveAll(String key, Map<String, String> value, Integer ttl) throws RedisRepositoryException {
        saveAll(key, value, ttl, null);
    }

    @Override
    @Durationable
    public void saveAll(String key, Map<String, String> value, Integer ttl, String requestId) throws RedisRepositoryException {
        LOG.debug("Save all -> Key: {} Values: {} TTL: {} RequestId: {}", key, value, ttl, requestId);
        try {
            jedisCluster.hmset(key, value);
            jedisCluster.expire(key, ttl);
        } catch (Exception e) {
            Meters.REDIS_ERROR_COUNTER_METER.incrementAndGet();
            if (shouldFallWhenError) {
                throw new RedisRepositoryException(e, Error.REDIS_SAVE_ALL_ERROR);
            } else {
                LOG.error("Error when save all to Redis. Key: {} Values: {}, TTL: {} RequestId: {}", key, value, ttl, requestId, e);
            }
        }
    }

    @Override
    @Durationable
    public Optional<String> read(String key) throws RedisRepositoryException {
        return read(key, null);
    }

    @Override
    @Durationable
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
            Meters.REDIS_ERROR_COUNTER_METER.incrementAndGet();
            if (shouldFallWhenError) {
                throw new RedisRepositoryException(e, Error.REDIS_READ_ERROR);
            } else {
                LOG.error("Error when read from Redis. Key: {} RequestId: {}", key, requestId, e);
                return Optional.empty();
            }
        }
    }

    @Override
    @Durationable
    public Optional<Map<String, String>> readAll(String key) throws RedisRepositoryException {
        return readAll(key, null);
    }

    @Override
    @Durationable
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
            Meters.REDIS_ERROR_COUNTER_METER.incrementAndGet();
            if (shouldFallWhenError) {
                throw new RedisRepositoryException(e, Error.REDIS_READ_ALL_ERROR);
            } else {
                LOG.error("Error when read all from Redis. Key: {} RequestId: {}", key, requestId, e);
                return Optional.empty();
            }
        }
    }

    @Override
    @Durationable
    public void remove(String key) throws RedisRepositoryException {
        remove(key, null);
    }

    @Override
    @Durationable
    public void remove(String key, String requestId) throws RedisRepositoryException {
        LOG.debug("Remove from Redis -> Key: {} RequestId: {}", key, requestId);
        try {
            jedisCluster.del(key);
        } catch (Exception e) {
            Meters.REDIS_ERROR_COUNTER_METER.incrementAndGet();
            if (shouldFallWhenError) {
                throw new RedisRepositoryException(e, Error.REDIS_REMOVE_ERROR);
            } else {
                LOG.error("Error when remove from Redis. Key: {} RequestId: {}", key, requestId, e);
            }
        }
    }
}
