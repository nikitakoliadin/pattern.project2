package com.qthegamep.pattern.project2.repository;

import com.qthegamep.pattern.project2.exception.RedisRepositoryException;

import java.util.Map;

public interface RedisRepository {

    void save(String key, String value) throws RedisRepositoryException;

    void save(String key, String value, String requestId) throws RedisRepositoryException;

    void save(String key, String value, Integer ttl) throws RedisRepositoryException;

    void save(String key, String value, Integer ttl, String requestId) throws RedisRepositoryException;

    void saveAll(String key, Map<String, String> value) throws RedisRepositoryException;

    void saveAll(String key, Map<String, String> value, String requestId) throws RedisRepositoryException;

    void saveAll(String key, Map<String, String> value, Integer ttl) throws RedisRepositoryException;

    void saveAll(String key, Map<String, String> value, Integer ttl, String requestId) throws RedisRepositoryException;
}
