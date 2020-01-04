package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.model.dto.ErrorResponse;
import com.qthegamep.pattern.project2.model.container.Error;

import java.util.List;
import java.util.Locale;

@FunctionalInterface
public interface ErrorResponseBuilderService {

    ErrorResponse buildResponse(Error error, List<Locale> requestLocales, String requestId);
}
