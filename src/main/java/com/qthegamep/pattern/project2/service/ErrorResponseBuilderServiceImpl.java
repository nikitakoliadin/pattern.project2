package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.model.dto.ErrorResponseDTO;
import com.qthegamep.pattern.project2.model.container.Error;
import com.qthegamep.pattern.project2.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class ErrorResponseBuilderServiceImpl implements ErrorResponseBuilderService {

    private static final Logger LOG = LoggerFactory.getLogger(ErrorResponseBuilderServiceImpl.class);

    private final List<Locale> availableLocales = new ArrayList<>();

    public ErrorResponseBuilderServiceImpl() {
        availableLocales.add(new Locale(Constants.UK_LANGUAGE.getValue()));
        availableLocales.add(new Locale(Constants.RU_LANGUAGE.getValue()));
    }

    @Override
    public ErrorResponseDTO buildResponse(Error error, List<Locale> requestLocales, String requestId) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        int errorCode = error.getErrorCode();
        LOG.debug("Response code: {} RequestId: {}", errorCode, requestId);
        errorResponseDTO.setErrorCode(errorCode);
        LOG.debug("Available locales: {} RequestId: {}", availableLocales, requestId);
        LOG.debug("Request locales: {} RequestId: {}", requestLocales, requestId);
        Locale locale = getLocale(requestLocales);
        LOG.debug("Locale: {} RequestId: {}", locale, requestId);
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(Constants.ERROR_MESSAGES_LOCALIZATION.getValue(), locale);
            String errorMessage = resourceBundle.getString(error.name());
            LOG.debug("Message: {} RequestId: {}", errorMessage, requestId);
            errorResponseDTO.setErrorMessage(errorMessage);
        } catch (Exception e) {
            LOG.error("Localization error. RequestId: {}", requestId, e);
            errorResponseDTO.setErrorMessage(error.name());
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
