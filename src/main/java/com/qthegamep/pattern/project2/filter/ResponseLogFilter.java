package com.qthegamep.pattern.project2.filter;

import org.glassfish.grizzly.http.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
@Priority(10)
public class ResponseLogFilter implements ContainerResponseFilter {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseLogFilter.class);

    @Context
    private javax.inject.Provider<Request> requestProvider;

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) {
        String path = containerRequestContext.getUriInfo().getRequestUri().toString();
        String requestId = containerRequestContext.getHeaderString("requestId");
        String clientIp = getClientIp();
        String duration = containerResponseContext.getHeaderString("duration");
        LOG.info("Request processed. Path: {} RequestId: {} Client IP: {} Duration: {}", path, requestId, clientIp, duration);
    }

    private String getClientIp() {
        Request request = requestProvider.get();
        return request.getHeader("X-Forwarded-For") == null
                ? request.getRemoteAddr()
                : request.getHeader("X-Forwarded-For");
    }
}
