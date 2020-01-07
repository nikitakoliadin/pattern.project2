package com.qthegamep.pattern.project2.filter;

import com.qthegamep.pattern.project2.statistics.Meters;
import com.qthegamep.pattern.project2.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Provider
@PreMatching
@Priority(20)
public class ResponseMetricsFilter implements ContainerResponseFilter {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseMetricsFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        String requestId = requestContext.getHeaderString(Constants.REQUEST_ID_HEADER);
        registerResponseStatusMetric(responseContext, requestId);
        registerRequestCounterMetric(requestContext, requestId);
        registerRequestTimeMetric(requestContext, responseContext, requestId);
        registerMaxRequestTimeMetric(requestContext, responseContext, requestId);
    }

    private void registerResponseStatusMetric(ContainerResponseContext responseContext, String requestId) {
        Response.StatusType responseStatusInfo = responseContext.getStatusInfo();
        long responseCodeCount = Meters.RESPONSE_STATUS_METER.get(String.valueOf(responseStatusInfo.getStatusCode())).incrementAndGet();
        LOG.debug("Response status: {} Code: {} Count: {} RequestId: {}", responseStatusInfo, responseStatusInfo.getStatusCode(), responseCodeCount, requestId);
    }

    private void registerRequestCounterMetric(ContainerRequestContext requestContext, String requestId) {
        String path = "/" + requestContext.getUriInfo().getPath();
        if (Meters.REQUEST_COUNTER_METER.containsKey(path)) {
            long requestCount = Meters.REQUEST_COUNTER_METER.get(path).incrementAndGet();
            LOG.debug("Path: [{}] Count: {} RequestId: {}", path, requestCount, requestId);
        } else {
            LOG.debug("Path: [{}] not exists. RequestId: {}", path, requestId);
        }
    }

    private void registerRequestTimeMetric(ContainerRequestContext requestContext, ContainerResponseContext responseContext, String requestId) {
        registerTimeMetric(Meters.REQUEST_TIME_METER, requestContext, responseContext, requestId);
    }

    private void registerMaxRequestTimeMetric(ContainerRequestContext requestContext, ContainerResponseContext responseContext, String requestId) {
        registerTimeMetric(Meters.MAX_REQUEST_TIME_METER, requestContext, responseContext, requestId);
    }

    private void registerTimeMetric(Map<String, List<AtomicLong>> metric, ContainerRequestContext requestContext, ContainerResponseContext responseContext, String requestId) {
        String path = "/" + requestContext.getUriInfo().getPath();
        if (metric.containsKey(path)) {
            AtomicLong duration = new AtomicLong(Long.parseLong(responseContext.getHeaderString(Constants.DURATION_HEADER)));
            metric.get(path).add(duration);
            LOG.debug("Path: [{}] Duration: {} RequestId: {}", path, duration, requestId);
        } else {
            LOG.debug("Path: [{}] not exists. RequestId: {}", path, requestId);
        }
    }
}
