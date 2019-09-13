package com.qthegamep.pattern.project2.filter;

import com.qthegamep.pattern.project2.service.GenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
@Priority(5)
public class RequestIdFilter implements ContainerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(RequestIdFilter.class);

    private GenerationService generationService;

    @Inject
    public RequestIdFilter(GenerationService generationService) {
        this.generationService = generationService;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        String requestId = containerRequestContext.getHeaderString("x-request-id") == null
                ? generationService.generateUniqueId(10L)
                : containerRequestContext.getHeaderString("x-request-id");
        LOG.debug("RequestId: {}", requestId);
        MultivaluedMap<String, String> headers = containerRequestContext.getHeaders();
        headers.add("requestId", requestId);
    }
}
