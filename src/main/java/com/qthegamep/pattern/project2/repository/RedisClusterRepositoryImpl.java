package com.qthegamep.pattern.project2.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

import javax.inject.Inject;

public class RedisClusterRepositoryImpl implements RedisRepository {

    private static final Logger LOG = LoggerFactory.getLogger(RedisClusterRepositoryImpl.class);

    private JedisCluster jedisCluster;

    @Inject
    public RedisClusterRepositoryImpl(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }
}
