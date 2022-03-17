package com.faltenreich.diaguard.feature.preference.data;

import com.faltenreich.diaguard.shared.Helper;

import java.util.Locale;

public class PreferenceCache {

    private static PreferenceCache instance;

    public static PreferenceCache getInstance() {
        if (instance == null) {
            instance = new PreferenceCache();
        }
        return instance;
    }

    private Locale locale;
    private int decimalPlaces;

    private PreferenceCache() {
        PreferenceStore preferenceStore = PreferenceStore.getInstance();
        setDecimalPlaces(preferenceStore.getDecimalPlaces());
        setLocale(Helper.getLocale());
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
