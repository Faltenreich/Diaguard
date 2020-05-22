package com.faltenreich.diaguard.feature.preference.factor;

import com.faltenreich.diaguard.feature.preference.data.TimeInterval;

class FactorListItem {

    private TimeInterval interval;
    private int hourOfDay;
    private float value;

    FactorListItem(TimeInterval interval, int hourOfDay, float value) {
        this.interval = interval;
        this.hourOfDay = hourOfDay;
        this.value = value;
    }

    TimeInterval getInterval() {
        return interval;
    }

    int getHourOfDay() {
        return hourOfDay;
    }

    float getValue() {
        return value;
    }

    void setValue(float value) {
        this.value = value;
    }
}
