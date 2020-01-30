package com.qthegamep.pattern.project2.exception.mapper;

import com.qthegamep.pattern.project2.binder.property.Property;
import com.qthegamep.pattern.project2.model.dto.ErrorResponse;
import com.qthegamep.pattern.project2.exception.ServiceException;
import com.qthegamep.pattern.project2.model.mapper.ErrorMapper;
import com.qthegamep.pattern.project2.statistics.Meters;
import com.qthegamep.pattern.project2.model.container.Error;
import com.qthegamep.pattern.project2.service.ErrorResponseBuilderService;
import com.qthegamep.pattern.project2.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@Provider
public class GeneralExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = LoggerFactory.getLogger(GeneralExceptionMapper.class);

    private boolean mongoDbSaveAllErrors;
    private String dockerImageName;
    private boolean exceptionMapperAppendDockerImageName;
    private boolean exceptionMapperAppendRequestId;
    private HttpHeaders httpHeaders;
    private ErrorResponseBuilderService errorResponseBuilderService;

    @Inject
    public GeneralExceptionMapper(@Property(value = "mongodb.save.all.errors") boolean mongoDbSaveAllErrors,
                                  @Property(value = "docker.image.name") String dockerImageName,
                                  @Property(value = "exception.mapper.append.docker.image.name") boolean exceptionMapperAppendDockerImageName,
                                  @Property(value = "exception.mapper.append.request.id") boolean exceptionMapperAppendRequestId,
                                  @Context HttpHeaders httpHeaders,
                                  ErrorResponseBuilderService errorResponseBuilderService) {
        this.mongoDbSaveAllErrors = mongoDbSaveAllErrors;
        this.dockerImageName = dockerImageName;
        this.exceptionMapperAppendDockerImageName = exceptionMapperAppendDockerImageName;
        this.exceptionMapperAppendRequestId = exceptionMapperAppendRequestId;
        this.httpHeaders = httpHeaders;
        this.errorResponseBuilderService = errorResponseBuilderService;
    }

    @Override
    public Response toResponse(Exception exception) {
        Error error = getError(exception);
        List<Locale> requestLocales = httpHeaders.getAcceptableLanguages();
        String requestId = httpHeaders.getHeaderString(Constants.REQUEST_ID_HEADER);
        LOG.error("Error. RequestId: {}", requestId, exception);
        ErrorResponse errorResponse = errorResponseBuilderService.buildResponse(error, requestLocales, requestId);
        saveError(errorResponse, requestId);
        errorResponse.setErrorMessage(buildErrorMessage(errorResponse.getErrorMessage(), error, exception, requestId));
        registerMetrics(error, requestId);
        return buildResponse(errorResponse);
    }

    private Error getError(Exception exception) {
        if (exception instanceof ServiceException) {
            return ((ServiceException) exception).getError();
        } else if (exception instanceof ValidationException) {
            return Error.INVALID_REQUEST_RESPONSE_ERROR;
        } else if (exception instanceof NotFoundException) {
            return Error.PAGE_NOT_FOUND_ERROR;
        } else {
            return Error.INTERNAL_ERROR;
        }
    }

    private void saveError(ErrorResponse errorResponse, String requestId) {
        LOG.debug("Should save error to database: {} RequestId: {}", mongoDbSaveAllErrors, requestId);
        if (mongoDbSaveAllErrors) {
            try {
                com.qthegamep.pattern.project2.model.entity.Error error = ErrorMapper.INSTANCE.errorResponseToError(errorResponse);
                error.setRequestId(requestId);
                // TODO: IMPLEMENTS SAVE ERROR
            } catch (Exception e) {
                LOG.error("Error while saving error to database. RequestId: {}", requestId, e);
            }
        }
    }

    private String buildErrorMessage(String errorResponseMessage, Error error, Exception exception, String requestId) {
        if (Error.INVALID_REQUEST_RESPONSE_ERROR.equals(error) || Error.VALIDATION_ERROR.equals(error)) {
            errorResponseMessage += " {" + exception.getMessage() + "}";
        }
        if (exceptionMapperAppendDockerImageName) {
            errorResponseMessage += " [POD: " + dockerImageName + "]";
        }
        if (exceptionMapperAppendRequestId) {
            errorResponseMessage += " Request ID: " + requestId;
        }
        return errorResponseMessage;
    }

    private void registerMetrics(Error error, String requestId) {
        String errorCode = String.valueOf(error.getErrorCode());
        long errorCount = Meters.ERROR_TYPES_METER.get(errorCode).incrementAndGet();
        LOG.debug("Error code: {} Error count: {} RequestId: {}", errorCode, errorCount, requestId);
    }

    private Response buildResponse(ErrorResponse errorResponse) {
        String contentType = getContentType(httpHeaders);
        if (errorResponse.getErrorCode() == Error.PAGE_NOT_FOUND_ERROR.getErrorCode()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .entity(errorResponse)
                    .build();
        }
    }

    private String getContentType(HttpHeaders httpHeaders) {
        String contentType = httpHeaders.getHeaderString(HttpHeaders.CONTENT_TYPE);
        LOG.debug("Content-Type: {}", contentType);
        if (contentType == null || contentType.isEmpty()) {
            contentType = MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.displayName()).toString();
            LOG.debug("New Content-Type: {}", contentType);
        }
        return contentType;
    }
}
