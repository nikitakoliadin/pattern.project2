package com.qthegamep.pattern.project2.config;

import com.qthegamep.pattern.project2.annotation.Property;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import java.lang.reflect.Type;
import java.util.Map;

public class PropertyInjectionResolver implements InjectionResolver<Property> {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyInjectionResolver.class);

    @Context
    private Application application;

    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> serviceHandle) {
        Type requiredType = injectee.getRequiredType();
        Property annotation = injectee.getParent().getAnnotation(Property.class);
        Map<String, Object> properties = application.getProperties();
        String key = annotation.value();
        Object value = properties.get(key);
        String defaultValue = annotation.defaultValue();
        LOG.debug("Property injection: Required Type: {} Key: {} Value: {} Default Value: {}", requiredType, key, value, defaultValue);
        if (value == null && !defaultValue.isEmpty()) {
            return defaultValue;
        } else {
            return value;
        }
    }

    @Override
    public boolean isConstructorParameterIndicator() {
        return false;
    }

    @Override
    public boolean isMethodParameterIndicator() {
        return false;
    }
}
