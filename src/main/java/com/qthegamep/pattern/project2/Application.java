package com.qthegamep.pattern.project2;

import com.qthegamep.pattern.project2.binder.ApplicationBinder;
import com.qthegamep.pattern.project2.config.ApplicationConfig;
import com.qthegamep.pattern.project2.config.IOStrategyFactory;
import com.qthegamep.pattern.project2.config.TaskQueueSizeLogProbe;
import com.qthegamep.pattern.project2.exception.ApplicationConfigInitializationException;
import com.qthegamep.pattern.project2.model.IoStrategyType;
import com.qthegamep.pattern.project2.util.Constants;
import org.glassfish.grizzly.http.server.*;
import org.glassfish.grizzly.monitoring.MonitoringConfig;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;
import org.glassfish.grizzly.threadpool.ThreadPoolProbe;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws ApplicationConfigInitializationException {
        initConfigs();
        String host = System.getProperty("application.host", "0.0.0.0");
        String port = System.getProperty("application.port", "8080");
        String applicationContext = System.getProperty("application.context", "");
        String applicationUrl = "http://" + host + ":" + port + applicationContext;
        HttpServer httpServer = startServer(applicationUrl);
        String swaggerUrl = System.getProperty("application.swagger.url", "/docs");
        addSwaggerUIMapping(httpServer, applicationContext + swaggerUrl);
    }

    private static void initConfigs() throws ApplicationConfigInitializationException {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.init();
    }

    private static HttpServer startServer(String applicationUrl) {
        URI applicationUri = URI.create(applicationUrl);
        ResourceConfig resourceConfig = new ResourceConfig()
                .packages(Application.class.getPackage().getName())
                .register(ApplicationBinder.builder().build());
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(applicationUri, resourceConfig, false);
        configServer(httpServer, applicationUrl);
        return httpServer;
    }

    private static void configServer(HttpServer httpServer, String applicationUrl) {
        try {
            ServerConfiguration serverConfiguration = httpServer.getServerConfiguration();
            serverConfiguration.setJmxEnabled(true);
            TCPNIOTransport grizzlyTransport = httpServer.getListener(Constants.GRIZZLY.getValue()).getTransport();
            ThreadPoolConfig threadPoolConfig = ThreadPoolConfig.defaultConfig()
                    .setPoolName(Constants.GRIZZLY_POOL_NAME.getValue())
                    .setCorePoolSize(32)
                    .setMaxPoolSize(32)
                    .setQueueLimit(-1);
            grizzlyTransport.configureBlocking(false);
            grizzlyTransport.setSelectorRunnersCount(Runtime.getRuntime().availableProcessors() * 4);
            grizzlyTransport.setWorkerThreadPoolConfig(threadPoolConfig);
            grizzlyTransport.setKernelThreadPoolConfig(threadPoolConfig);
            grizzlyTransport.setIOStrategy(new IOStrategyFactory().createIOStrategy(IoStrategyType.DYNAMIC_IO_STRATEGY));
            addProbes(grizzlyTransport);
            httpServer.start();
            LOG.info("\nBlocking Transport(T/F): {}\nNum SelectorRunners: {}\nNum WorkerThreads: {}\nNum KernelThreadPool: {}\nQueue limit: {}",
                    grizzlyTransport.isBlocking(),
                    grizzlyTransport.getSelectorRunnersCount(),
                    grizzlyTransport.getWorkerThreadPoolConfig().getCorePoolSize(),
                    grizzlyTransport.getKernelThreadPoolConfig().getCorePoolSize(),
                    grizzlyTransport.getWorkerThreadPoolConfig().getQueueLimit());
            LOG.info("{} application started at {}", Application.class.getPackage().getName(), applicationUrl);
        } catch (Exception e) {
            LOG.error("Error", e);
        }
    }

    private static void addProbes(TCPNIOTransport grizzlyTransport) {
        MonitoringConfig<ThreadPoolProbe> threadPoolMonitoringConfig = grizzlyTransport.getThreadPoolMonitoringConfig();
        threadPoolMonitoringConfig.addProbes(new TaskQueueSizeLogProbe());
        LOG.info("Probes was added");
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
        NetworkListener grizzlyListener = httpServer.getListener(Constants.GRIZZLY.getValue());
        String swaggerPath = "http://" + grizzlyListener.getHost() + ":" + grizzlyListener.getPort() + contextPath + urlPattern;
        LOG.info("Swagger openApi available at {}", swaggerPath);
    }
}
