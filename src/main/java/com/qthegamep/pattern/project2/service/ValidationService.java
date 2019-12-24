package com.qthegamep.pattern.project2.service;

@FunctionalInterface
public interface ValidationService {

    <T> void validate(T object);
}
