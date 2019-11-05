package com.qthegamep.pattern.project2.filter;

import com.qthegamep.pattern.project2.metric.Metrics;
import com.qthegamep.pattern.project2.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
@Priority(30)
public class ResponseStatusMetricFilter implements ContainerResponseFilter {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseStatusMetricFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        String requestId = requestContext.getHeaderString(Constants.REQUEST_ID_HEADER.getValue());
        Response.StatusType responseStatusInfo = responseContext.getStatusInfo();
        long responseCodeCount = Metrics.RESPONSE_STATUS_METRIC.get(String.valueOf(responseStatusInfo.getStatusCode())).incrementAndGet();
        LOG.debug("Response status: {} Code: {} Count: {} RequestId: {}", responseStatusInfo, responseStatusInfo.getStatusCode(), responseCodeCount, requestId);
    }
}
