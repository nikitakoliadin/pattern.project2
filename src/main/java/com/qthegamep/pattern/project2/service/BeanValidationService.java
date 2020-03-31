package com.qthegamep.pattern.project2.service;

@FunctionalInterface
public interface BeanValidationService {

    <T> void validateBean(T object);
}
