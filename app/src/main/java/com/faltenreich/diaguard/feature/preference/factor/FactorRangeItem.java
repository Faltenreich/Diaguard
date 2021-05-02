package com.faltenreich.diaguard.feature.preference.factor;

class FactorRangeItem {

    private final int hourOfDay;
    private final int rangeInHours;
    private float value;

    FactorRangeItem(int hourOfDay, int rangeInHours, float value) {
        this.hourOfDay = hourOfDay;
        this.rangeInHours = rangeInHours;
        this.value = value;
    }

    int getHourOfDay() {
        return hourOfDay;
    }

    int getRangeInHours() {
        return rangeInHours;
    }

    float getValue() {
        return value;
    }

    void setValue(float value) {
        this.value = value;
    }
}
