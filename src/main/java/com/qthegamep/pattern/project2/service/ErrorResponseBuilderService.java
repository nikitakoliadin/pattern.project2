package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.model.dto.ErrorResponseDTO;
import com.qthegamep.pattern.project2.model.container.ErrorType;

import java.util.List;
import java.util.Locale;

@FunctionalInterface
public interface ErrorResponseBuilderService {

    ErrorResponseDTO buildResponse(ErrorType errorType, List<Locale> requestLocales, String requestId);
}
