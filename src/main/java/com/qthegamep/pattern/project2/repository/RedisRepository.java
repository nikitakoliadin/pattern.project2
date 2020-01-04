package com.qthegamep.pattern.project2.repository;

import com.qthegamep.pattern.project2.exception.compile.RedisRepositoryException;

import java.util.Map;
import java.util.Optional;

public interface RedisRepository {

    void save(String key, String value) throws RedisRepositoryException;

    void save(String key, String value, String requestId) throws RedisRepositoryException;

    void save(String key, String value, Integer ttl) throws RedisRepositoryException;

    void save(String key, String value, Integer ttl, String requestId) throws RedisRepositoryException;

    void saveAll(String key, Map<String, String> value) throws RedisRepositoryException;

    void saveAll(String key, Map<String, String> value, String requestId) throws RedisRepositoryException;

    void saveAll(String key, Map<String, String> value, Integer ttl) throws RedisRepositoryException;

    void saveAll(String key, Map<String, String> value, Integer ttl, String requestId) throws RedisRepositoryException;

    Optional<String> read(String key) throws RedisRepositoryException;

    Optional<String> read(String key, String requestId) throws RedisRepositoryException;

    Optional<Map<String, String>> readAll(String key) throws RedisRepositoryException;

    Optional<Map<String, String>> readAll(String key, String requestId) throws RedisRepositoryException;
}
