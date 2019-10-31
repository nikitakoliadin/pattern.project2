package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.dto.ErrorResponseDTO;
import com.qthegamep.pattern.project2.model.ErrorType;
import com.qthegamep.pattern.project2.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class ErrorResponseBuilderServiceImpl implements ErrorResponseBuilderService {

    private static final Logger LOG = LoggerFactory.getLogger(ErrorResponseBuilderServiceImpl.class);

    private final List<Locale> availableLocales = new ArrayList<>();

    public ErrorResponseBuilderServiceImpl() {
        availableLocales.add(new Locale("uk"));
        availableLocales.add(new Locale("ru"));
    }

    @Override
    public ErrorResponseDTO buildResponse(ErrorType errorType, List<Locale> requestLocales, String requestId) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        int errorCode = errorType.getErrorCode();
        LOG.debug("Response code: {} RequestId: {}", errorCode, requestId);
        errorResponseDTO.setErrorCode(errorCode);
        LOG.debug("Available locales: {} RequestId: {}", availableLocales, requestId);
        LOG.debug("Request locales: {} RequestId: {}", requestLocales, requestId);
        Locale locale = getLocale(requestLocales);
        LOG.debug("Locale: {} RequestId: {}", locale, requestId);
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(Constants.ERROR_MESSAGES_LOCALIZATION.getValue(), locale);
            String errorMessage = resourceBundle.getString(errorType.name());
            LOG.debug("Message: {} RequestId: {}", errorMessage, requestId);
            errorResponseDTO.setErrorMessage(errorMessage);
        } catch (Exception e) {
            LOG.error("Localization error. RequestId: {}", requestId, e);
            errorResponseDTO.setErrorMessage(errorType.name());
        }
        return errorResponseDTO;
    }

    private Locale getLocale(List<Locale> requestLocales) {
        if (requestLocales == null) {
            return new Locale(Constants.DEFAULT_LANGUAGE.getValue());
        }
        List<Locale> requestAvailableLocales = requestLocales.stream()
                .filter(availableLocales::contains)
                .collect(Collectors.toList());
        if (requestAvailableLocales.isEmpty()) {
            return new Locale(Constants.DEFAULT_LANGUAGE.getValue());
        } else {
            String language = requestAvailableLocales.get(0).getLanguage();
            return new Locale(language);
        }
    }
}
