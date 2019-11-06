package com.qthegamep.pattern.project2.filter;

import com.qthegamep.pattern.project2.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.container.*;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
@Priority(30)
public class DurationResponseFilter implements ContainerResponseFilter {

    private static final Logger LOG = LoggerFactory.getLogger(DurationResponseFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) {
        String requestId = containerRequestContext.getHeaderString(Constants.REQUEST_ID_HEADER.getValue());
        String startTime = containerRequestContext.getHeaderString(Constants.START_TIME_HEADER.getValue());
        long duration = System.currentTimeMillis() - Long.parseLong(startTime);
        LOG.debug("Duration: {} RequestId: {}", duration, requestId);
        MultivaluedMap<String, Object> headers = containerResponseContext.getHeaders();
        headers.add(Constants.DURATION_HEADER.getValue(), duration);
    }
}
