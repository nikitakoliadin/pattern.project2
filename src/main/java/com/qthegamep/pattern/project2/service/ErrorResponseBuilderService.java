package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.dto.ErrorResponseDTO;
import com.qthegamep.pattern.project2.model.ErrorType;

import java.util.List;
import java.util.Locale;

@FunctionalInterface
public interface ErrorResponseBuilderService {

    ErrorResponseDTO buildResponse(ErrorType errorType, List<Locale> requestLocales, String requestId);
}
