package com.faltenreich.diaguard.feature.preference.data;

public class PreferenceCache {

    private static PreferenceCache instance;

    public static PreferenceCache getInstance() {
        if (instance == null) {
            instance = new PreferenceCache();
        }
        return instance;
    }

    private int decimalPlaces;

    private PreferenceCache() {
        PreferenceStore preferenceStore = PreferenceStore.getInstance();
        setDecimalPlaces(preferenceStore.getDecimalPlaces());
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }
}
