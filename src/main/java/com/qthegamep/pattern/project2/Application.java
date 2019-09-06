package com.qthegamep.pattern.project2;

import com.qthegamep.pattern.project2.binder.ApplicationBinder;
import com.qthegamep.pattern.project2.config.ApplicationConfig;
import com.qthegamep.pattern.project2.exception.ApplicationConfigException;
import org.glassfish.grizzly.http.server.*;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws ApplicationConfigException {
        ApplicationConfig.init();
        String host = System.getProperty("application.host", "0.0.0.0");
        String port = System.getProperty("application.port", "8080");
        String applicationContext = System.getProperty("application.context", "");
        String applicationUrl = "http://" + host + ":" + port + applicationContext;
        HttpServer httpServer = startServer(applicationUrl);
        LOG.info("{} application started at {}", Application.class.getPackage().getName(), applicationUrl);
        String swaggerUrl = System.getProperty("application.swagger.url", "/docs");
        addSwaggerUIMapping(httpServer, applicationContext + swaggerUrl);
    }

    private static HttpServer startServer(String applicationUrl) {
        URI applicationUri = URI.create(applicationUrl);
        ResourceConfig resourceConfig = new ResourceConfig()
                .packages(Application.class.getPackage().getName())
                .register(ApplicationBinder.class);
        return GrizzlyHttpServerFactory.createHttpServer(applicationUri, resourceConfig);
    }

    private static void addSwaggerUIMapping(HttpServer httpServer, String contextPath) {
        ClassLoader classLoader = Application.class.getClassLoader();
        CLStaticHttpHandler docsStaticHttpHandler = new CLStaticHttpHandler(classLoader, "swagger-ui/");
        docsStaticHttpHandler.setFileCacheEnabled(false);
        String urlPattern = "/";
        HttpHandlerRegistration httpHandlerRegistration = HttpHandlerRegistration.builder()
                .contextPath(contextPath)
                .urlPattern(urlPattern)
                .build();
        ServerConfiguration serverConfiguration = httpServer.getServerConfiguration();
        serverConfiguration.addHttpHandler(docsStaticHttpHandler, httpHandlerRegistration);
        NetworkListener grizzlyListener = httpServer.getListener("grizzly");
        String swaggerPath = "http://" + grizzlyListener.getHost() + ":" + grizzlyListener.getPort() + contextPath + urlPattern;
        LOG.info("Swagger openApi available at {}", swaggerPath);
    }
}
