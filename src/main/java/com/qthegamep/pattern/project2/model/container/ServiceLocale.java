package com.qthegamep.pattern.project2.model.container;

import java.util.Locale;

public enum ServiceLocale {

    UK_LOCALE(new Locale("uk")),
    RU_LOCALE(new Locale("ru"));

    private Locale locale;

    ServiceLocale(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }
}
