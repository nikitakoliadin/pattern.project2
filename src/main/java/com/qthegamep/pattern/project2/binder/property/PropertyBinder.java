package com.qthegamep.pattern.project2.binder.property;

import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class PropertyBinder extends AbstractBinder {

    private PropertyInjectionResolver propertyInjectionResolver;

    private PropertyBinder(PropertyInjectionResolver propertyInjectionResolver) {
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

        public PropertyBinder build() {
            return new PropertyBinder(propertyInjectionResolver);
        }
    }
}
