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
@Priority(20)
public class DurationRequestFilter implements ContainerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(DurationRequestFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        String requestId = containerRequestContext.getHeaderString(Constants.REQUEST_ID_HEADER.getValue());
        MultivaluedMap<String, String> headers = containerRequestContext.getHeaders();
        String startTime = String.valueOf(System.currentTimeMillis());
        LOG.debug("StartTime: {} RequestId: {}", startTime, requestId);
        headers.add(Constants.START_TIME_HEADER.getValue(), startTime);
    }
}
