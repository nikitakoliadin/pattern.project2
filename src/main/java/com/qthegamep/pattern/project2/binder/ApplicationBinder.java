package com.qthegamep.pattern.project2.binder;

import com.qthegamep.pattern.project2.service.GenerationService;
import com.qthegamep.pattern.project2.service.GenerationServiceImpl;
import org.glassfish.jersey.internal.inject.AbstractBinder;

import javax.inject.Singleton;
import java.util.Objects;

public class ApplicationBinder extends AbstractBinder {

    private GenerationService generationService;

    private ApplicationBinder(GenerationService generationService) {
        this.generationService = generationService;
    }

    public GenerationService getGenerationService() {
        return generationService;
    }

    public static ApplicationBinderBuilder builder() {
        return new ApplicationBinderBuilder();
    }

    @Override
    protected void configure() {
        bindGenerationService();
    }

    private void bindGenerationService() {
        if (Objects.isNull(generationService)) {
            bind(GenerationServiceImpl.class).to(GenerationService.class).in(Singleton.class);
        } else {
            bind(generationService).to(GenerationService.class).in(Singleton.class);
        }
    }

    public static class ApplicationBinderBuilder {

        private GenerationService generationService;

        public ApplicationBinderBuilder setGenerationService(GenerationService generationService) {
            this.generationService = generationService;
            return this;
        }

        public ApplicationBinder build() {
            return new ApplicationBinder(generationService);
        }
    }
}
