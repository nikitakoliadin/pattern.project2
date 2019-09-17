package com.qthegamep.pattern.project2.mapper;

import com.qthegamep.pattern.project2.dto.ErrorResponseDTO;
import com.qthegamep.pattern.project2.model.ResponseCode;
import com.qthegamep.pattern.project2.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BaseExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseExceptionMapper.class);

    @Context
    private HttpHeaders httpHeaders;

    @Override
    public Response toResponse(Exception exception) {
        String requestId = httpHeaders.getHeaderString(Constants.REQUEST_ID_HEADER.getValue());
        ResponseCode responseCode = ResponseCode.INTERNAL_ERROR;
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setRequestId(requestId);
        errorResponseDTO.setResponseCode(responseCode);
        LOG.error("Error processing request", exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponseDTO)
                .build();
    }
}
