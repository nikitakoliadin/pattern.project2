package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.model.container.ServiceLocale;
import com.qthegamep.pattern.project2.model.dto.ErrorResponse;
import com.qthegamep.pattern.project2.model.container.Error;
import com.qthegamep.pattern.project2.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class ErrorResponseBuilderServiceImpl implements ErrorResponseBuilderService {

    private static final Logger LOG = LoggerFactory.getLogger(ErrorResponseBuilderServiceImpl.class);

    private final List<Locale> availableLocales;
    private final Locale defaultLocale;

    public ErrorResponseBuilderServiceImpl() {
        availableLocales = Arrays.stream(ServiceLocale.values())
                .map(ServiceLocale::getLocale)
                .collect(Collectors.toList());
        defaultLocale = ServiceLocale.values()[0].getLocale();
    }

    @Override
    public ErrorResponse buildResponse(Error error, List<Locale> requestLocales, String requestId) {
        ErrorResponse errorResponse = new ErrorResponse();
        int errorCode = error.getErrorCode();
        LOG.debug("Response code: {} RequestId: {}", errorCode, requestId);
        errorResponse.setErrorCode(errorCode);
        LOG.debug("Available locales: {} RequestId: {}", availableLocales, requestId);
        LOG.debug("Request locales: {} RequestId: {}", requestLocales, requestId);
        Locale locale = getLocale(requestLocales);
        LOG.debug("Locale: {} RequestId: {}", locale, requestId);
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(Constants.ERROR_MESSAGES_LOCALIZATION, locale);
            String errorMessage = resourceBundle.getString(error.name());
            LOG.debug("Message: {} RequestId: {}", errorMessage, requestId);
            errorResponse.setErrorMessage(errorMessage);
        } catch (Exception e) {
            LOG.error("Localization error. RequestId: {}", requestId, e);
            errorResponse.setErrorMessage(error.name());
        }
        return errorResponse;
    }

    private Locale getLocale(List<Locale> requestLocales) {
        if (requestLocales == null) {
            return defaultLocale;
        }
        List<Locale> requestAvailableLocales = requestLocales.stream()
                .filter(availableLocales::contains)
                .collect(Collectors.toList());
        if (requestAvailableLocales.isEmpty()) {
            return defaultLocale;
        } else {
            String language = requestAvailableLocales.get(0).getLanguage();
            return new Locale(language);
        }
    }
}
