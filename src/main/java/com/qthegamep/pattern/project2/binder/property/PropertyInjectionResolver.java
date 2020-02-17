package com.qthegamep.pattern.project2.binder.property;

import com.google.common.base.Defaults;
import com.qthegamep.pattern.project2.exception.runtime.PropertyInjectionResolverRuntimeException;
import org.apache.commons.lang3.ClassUtils;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

@Singleton
public class PropertyInjectionResolver implements InjectionResolver<Property> {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyInjectionResolver.class);

    @Context
    private Application application;

    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> serviceHandle) {
        Type requiredType = injectee.getRequiredType();
        Property annotation = getPropertyAnnotation(injectee);
        Map<String, Object> properties = application.getProperties();
        String key = annotation.value();
        Object value = properties.get(key);
        String defaultValue = annotation.defaultValue();
        LOG.debug("Property injection: Required Type: {} Key: {} Value: {} Default Value: {}", requiredType, key, value, defaultValue);
        if (value != null) {
            return parse(value, requiredType);
        } else if (!defaultValue.isEmpty()) {
            return parse(defaultValue, requiredType);
        } else {
            return emptyFor(requiredType);
        }
    }

    @Override
    public boolean isConstructorParameterIndicator() {
        return true;
    }

    @Override
    public boolean isMethodParameterIndicator() {
        return true;
    }

    private Property getPropertyAnnotation(Injectee injectee) {
        Property annotation = injectee.getParent().getAnnotation(Property.class);
        if (annotation == null) {
            for (Annotation requiredQualifier : injectee.getRequiredQualifiers()) {
                if (requiredQualifier instanceof Property) {
                    annotation = (Property) requiredQualifier;
                    break;
                }
            }
        }
        if (annotation == null) {
            throw new PropertyInjectionResolverRuntimeException("Can't find Property annotation!");
        }
        return annotation;
    }

    private Object parse(Object value, Type requiredType) {
        Class<?> requiredClass = (Class<?>) requiredType;
        if (isPrimitive(requiredClass)) {
            LOG.debug("Parse to primitive value");
            return parseToPrimitive(value.toString(), requiredClass);
        } else if (isPrimitiveWrapper(requiredClass)) {
            LOG.debug("Parse to primitive wrapper value");
            return parseToPrimitive(value.toString(), requiredClass);
        } else {
            LOG.debug("Parse to object value");
            return parseToObject(value, requiredClass);
        }
    }

    private Object emptyFor(Type requiredType) {
        Class<?> requiredClass = (Class<?>) requiredType;
        if (isPrimitive(requiredClass)) {
            LOG.debug("Empty primitive value");
            return Defaults.defaultValue(requiredClass);
        } else {
            LOG.debug("Empty object value");
            return null;
        }
    }

    private boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive();
    }

    private boolean isPrimitiveWrapper(Class<?> clazz) {
        return ClassUtils.isPrimitiveWrapper(clazz);
    }

    private Object parseToPrimitive(String value, Class<?> requiredClass) {
        if (requiredClass == Boolean.class || requiredClass == Boolean.TYPE) {
            return Boolean.parseBoolean(value);
        } else if (requiredClass == Character.class || requiredClass == Character.TYPE) {
            return value.isEmpty()
                    ? '\u0000'
                    : value.charAt(0);
        } else if (requiredClass == Byte.class || requiredClass == Byte.TYPE) {
            return Byte.parseByte(value);
        } else if (requiredClass == Short.class || requiredClass == Short.TYPE) {
            return Short.parseShort(value);
        } else if (requiredClass == Integer.class || requiredClass == Integer.TYPE) {
            return Integer.parseInt(value);
        } else if (requiredClass == Long.class || requiredClass == Long.TYPE) {
            return Long.parseLong(value);
        } else if (requiredClass == Float.class || requiredClass == Float.TYPE) {
            return Float.parseFloat(value);
        } else if (requiredClass == Double.class || requiredClass == Double.TYPE) {
            return Double.parseDouble(value);
        } else if (requiredClass == Void.class || requiredClass == Void.TYPE) {
            return Void.TYPE;
        } else {
            LOG.warn("Something was wrong when parse value: {} to primitive class: {}", value, requiredClass);
            return null;
        }
    }

    private Object parseToObject(Object value, Class<?> requiredClass) {
        if (requiredClass == String.class) {
            return value;
        } else {
            LOG.warn("Something was wrong when parse value: {} to object class: {}", value, requiredClass);
            return null;
        }
    }
}
