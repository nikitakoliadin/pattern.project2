package com.qthegamep.pattern.project2.binder;

import com.qthegamep.pattern.project2.annotation.Property;
import com.qthegamep.pattern.project2.config.PropertyInjectionResolver;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class ConfigurationBinder extends AbstractBinder {

    private PropertyInjectionResolver propertyInjectionResolver;

    private ConfigurationBinder(PropertyInjectionResolver propertyInjectionResolver) {
        this.propertyInjectionResolver = propertyInjectionResolver;
    }

    public PropertyInjectionResolver getPropertyInjectionResolver() {
        return propertyInjectionResolver;
    }

    public static ConfigurationBinderBuilder builder() {
        return new ConfigurationBinderBuilder();
    }

    @Override
    protected void configure() {
        bindConfigurationInjectionResolver();
    }

    private void bindConfigurationInjectionResolver() {
        if (propertyInjectionResolver == null) {
            bind(PropertyInjectionResolver.class).to(new TypeLiteral<InjectionResolver<Property>>() {
            }).in(Singleton.class);
        } else {
            bind(propertyInjectionResolver).to(new TypeLiteral<InjectionResolver<Property>>() {
            });
        }
    }

    public static class ConfigurationBinderBuilder {

        private PropertyInjectionResolver propertyInjectionResolver;

        public ConfigurationBinderBuilder setPropertyInjectionResolver(PropertyInjectionResolver propertyInjectionResolver) {
            this.propertyInjectionResolver = propertyInjectionResolver;
            return this;
        }

        public ConfigurationBinder build() {
            return new ConfigurationBinder(propertyInjectionResolver);
        }
    }
}
