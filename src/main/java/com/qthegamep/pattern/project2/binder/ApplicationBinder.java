package com.qthegamep.pattern.project2.binder;

import com.qthegamep.pattern.project2.service.GenerationService;
import com.qthegamep.pattern.project2.service.GenerationServiceImpl;
import com.qthegamep.pattern.project2.service.ConverterService;
import com.qthegamep.pattern.project2.service.ConverterServiceImpl;
import org.glassfish.jersey.internal.inject.AbstractBinder;

import javax.inject.Singleton;
import java.util.Objects;

public class ApplicationBinder extends AbstractBinder {

    private ConverterService converterService;
    private GenerationService generationService;

    private ApplicationBinder(ConverterService converterService, GenerationService generationService) {
        this.converterService = converterService;
        this.generationService = generationService;
    }

    public ConverterService getConverterService() {
        return converterService;
    }

    public GenerationService getGenerationService() {
        return generationService;
    }

    public static ApplicationBinderBuilder builder() {
        return new ApplicationBinderBuilder();
    }

    @Override
    protected void configure() {
        bindConverterService();
        bindGenerationService();
    }

    private void bindConverterService() {
        if (Objects.isNull(converterService)) {
            bind(ConverterServiceImpl.class).to(ConverterService.class).in(Singleton.class);
        } else {
            bind(converterService).to(ConverterService.class).in(Singleton.class);
        }
    }

    private void bindGenerationService() {
        if (Objects.isNull(generationService)) {
            bind(GenerationServiceImpl.class).to(GenerationService.class).in(Singleton.class);
        } else {
            bind(generationService).to(GenerationService.class).in(Singleton.class);
        }
    }

    public static class ApplicationBinderBuilder {

        private ConverterService converterService;
        private GenerationService generationService;

        public ApplicationBinderBuilder setConverterService(ConverterService converterService) {
            this.converterService = converterService;
            return this;
        }

        public ApplicationBinderBuilder setGenerationService(GenerationService generationService) {
            this.generationService = generationService;
            return this;
        }

        public ApplicationBinder build() {
            return new ApplicationBinder(converterService, generationService);
        }
    }
}
