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
@Priority(20)
public class ResponseMetricsFilter implements ContainerResponseFilter {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseMetricsFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        String requestId = requestContext.getHeaderString(Constants.REQUEST_ID_HEADER.getValue());
        registerResponseStatusMetric(responseContext, requestId);
        registerRequestCounterMetric(requestContext, requestId);
    }

    private void registerResponseStatusMetric(ContainerResponseContext responseContext, String requestId) {
        Response.StatusType responseStatusInfo = responseContext.getStatusInfo();
        long responseCodeCount = Metrics.RESPONSE_STATUS_METRIC.get(String.valueOf(responseStatusInfo.getStatusCode())).incrementAndGet();
        LOG.debug("Response status: {} Code: {} Count: {} RequestId: {}", responseStatusInfo, responseStatusInfo.getStatusCode(), responseCodeCount, requestId);
    }

    private void registerRequestCounterMetric(ContainerRequestContext requestContext, String requestId) {
        String path = "/" + requestContext.getUriInfo().getPath();
        if (Metrics.REQUEST_COUNTER_METRIC.containsKey(path)) {
            long requestCount = Metrics.REQUEST_COUNTER_METRIC.get(path).incrementAndGet();
            LOG.debug("Path: [{}] Count: {} RequestId: {}", path, requestCount, requestId);
        } else {
            LOG.debug("Path: [{}] not exists. RequestId: {}", path, requestId);
        }
    }
}
