package com.qthegamep.pattern.project2.filter;

import com.qthegamep.pattern.project2.service.GenerationService;
import com.qthegamep.pattern.project2.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.*;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
@Priority(10)
public class RequestIdFilter implements ContainerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(RequestIdFilter.class);

    private GenerationService generationService;

    @Inject
    public RequestIdFilter(GenerationService generationService) {
        this.generationService = generationService;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        String requestIdHeader = containerRequestContext.getHeaderString(Constants.REQUEST_ID_HEADER.getValue());
        if (requestIdHeader == null || requestIdHeader.isEmpty()) {
            String requestId = containerRequestContext.getHeaderString(Constants.X_REQUEST_ID_HEADER.getValue()) == null
                    ? generationService.generateUniqueId(10L)
                    : containerRequestContext.getHeaderString(Constants.X_REQUEST_ID_HEADER.getValue());
            LOG.debug("RequestId: {}", requestId);
            MultivaluedMap<String, String> headers = containerRequestContext.getHeaders();
            headers.add(Constants.REQUEST_ID_HEADER.getValue(), requestId);
        } else {
            LOG.debug("RequestId header: {}", requestIdHeader);
        }
    }
}
