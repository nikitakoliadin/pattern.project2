package com.qthegamep.pattern.project2.filter;

import com.qthegamep.pattern.project2.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
@Priority(40)
public class RequestIdResponseFilter implements ContainerResponseFilter {

    private static final Logger LOG = LoggerFactory.getLogger(RequestIdResponseFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        String requestId = requestContext.getHeaderString(Constants.REQUEST_ID_HEADER);
        LOG.debug("RequestId: {}", requestId);
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        headers.add(Constants.REQUEST_ID_HEADER, requestId);
    }
}
