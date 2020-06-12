package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.model.container.ServiceLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class LocaleServiceImpl implements LocaleService {

    private static final Logger LOG = LoggerFactory.getLogger(LocaleServiceImpl.class);

    private List<Locale> availableLocales;
    private Locale defaultLocale;

    @Inject
    public LocaleServiceImpl() {
        availableLocales = Arrays.stream(ServiceLocale.values())
                .map(ServiceLocale::getLocale)
                .collect(Collectors.toList());
        defaultLocale = ServiceLocale.UK_LOCALE.getLocale();
    }

    @Override
    public Locale getLocale(List<Locale> requestLocales, String requestId) {
        LOG.debug("Request locales: {} Available locales: {} Default locale: {} RequestId: {}", requestLocales, availableLocales, defaultLocale, requestId);
        if (requestLocales == null) {
            return defaultLocale;
        }
        List<Locale> requestAvailableLocales = requestLocales.stream()
                .filter(availableLocales::contains)
                .collect(Collectors.toList());
        if (requestAvailableLocales.isEmpty()) {
            return defaultLocale;
        } else {
            return requestAvailableLocales.get(0);
        }
    }
}
