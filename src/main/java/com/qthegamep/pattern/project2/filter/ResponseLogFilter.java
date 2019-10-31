package com.qthegamep.pattern.project2.filter;

import com.qthegamep.pattern.project2.util.Constants;
import org.glassfish.grizzly.http.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
@Priority(10)
public class ResponseLogFilter implements ContainerResponseFilter {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseLogFilter.class);

    private javax.inject.Provider<Request> requestProvider;

    @Inject
    public ResponseLogFilter(@Context javax.inject.Provider<Request> requestProvider) {
        this.requestProvider = requestProvider;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) {
        String path = containerRequestContext.getUriInfo().getRequestUri().toString();
        String requestId = containerRequestContext.getHeaderString(Constants.REQUEST_ID_HEADER.getValue());
        String clientIp = getClientIp();
        String duration = containerResponseContext.getHeaderString(Constants.DURATION_HEADER.getValue());
        LOG.info("Request processed. Path: {} RequestId: {} Client IP: {} Duration: {}", path, requestId, clientIp, duration);
    }

    private String getClientIp() {
        Request request = requestProvider.get();
        return request.getHeader(Constants.X_FORWARDED_FOR_HEADER.getValue()) == null
                ? request.getRemoteAddr()
                : request.getHeader(Constants.X_FORWARDED_FOR_HEADER.getValue());
    }
}
