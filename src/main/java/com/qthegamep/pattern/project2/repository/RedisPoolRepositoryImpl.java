package com.qthegamep.pattern.project2.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;

import javax.inject.Inject;

public class RedisPoolRepositoryImpl implements RedisRepository {

    private static final Logger LOG = LoggerFactory.getLogger(RedisPoolRepositoryImpl.class);

    private JedisPool jedisPool;

    @Inject
    public RedisPoolRepositoryImpl(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
