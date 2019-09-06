package com.qthegamep.pattern.project2.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.security.SecureRandom;

@Provider
@PreMatching
@Priority(5)
public class RequestIdFilter implements ContainerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(RequestIdFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        String requestId = containerRequestContext.getHeaderString("x-request-id") == null
                ? generateRequestId(10)
                : containerRequestContext.getHeaderString("x-request-id");
        LOG.debug("RequestId: {}", requestId);
        MultivaluedMap<String, String> headers = containerRequestContext.getHeaders();
        headers.add("requestId", requestId);
    }

    private static String generateRequestId(int length) {
        StringBuilder result = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; ++i) {
            boolean upperCase = secureRandom.nextBoolean();
            char symbol = (char) (secureRandom.nextInt(26) + 97);
            if (upperCase) {
                symbol = Character.toUpperCase(symbol);
            }
            result.append(symbol);
        }
        return result.toString();
    }
}
