package com.qthegamep.pattern.project2.service;

import java.util.List;
import java.util.Locale;

@FunctionalInterface
public interface LocaleService {

    Locale getLocale(List<Locale> requestLocales, String requestId);
}
