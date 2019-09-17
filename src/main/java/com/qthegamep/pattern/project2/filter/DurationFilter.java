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
@Priority(15)
public class DurationFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOG = LoggerFactory.getLogger(DurationFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        MultivaluedMap<String, String> headers = containerRequestContext.getHeaders();
        String startTime = String.valueOf(System.currentTimeMillis());
        LOG.debug("StartTime: {}", startTime);
        headers.add(Constants.START_TIME_HEADER.getValue(), startTime);
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) {
        String startTime = containerRequestContext.getHeaderString(Constants.START_TIME_HEADER.getValue());
        long duration = System.currentTimeMillis() - Long.parseLong(startTime);
        LOG.debug("Duration: {}", duration);
        MultivaluedMap<String, Object> headers = containerResponseContext.getHeaders();
        headers.add(Constants.DURATION_HEADER.getValue(), duration);
    }
}
