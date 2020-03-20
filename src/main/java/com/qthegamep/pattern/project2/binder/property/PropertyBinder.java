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

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected void configure() {
        bindPropertyInjectionResolver();
    }

    private void bindPropertyInjectionResolver() {
        if (propertyInjectionResolver == null) {
            bind(PropertyInjectionResolver.class).to(new TypeLiteral<InjectionResolver<Property>>() {
            }).in(Singleton.class);
        } else {
            bind(propertyInjectionResolver).to(new TypeLiteral<InjectionResolver<Property>>() {
            });
        }
    }

    public static class Builder {

        private PropertyInjectionResolver propertyInjectionResolver;

        public Builder setPropertyInjectionResolver(PropertyInjectionResolver propertyInjectionResolver) {
            this.propertyInjectionResolver = propertyInjectionResolver;
            return this;
        }

        public PropertyBinder build() {
            return new PropertyBinder(propertyInjectionResolver);
        }
    }
}
