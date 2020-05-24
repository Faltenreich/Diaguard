package com.faltenreich.diaguard.feature.preference.factor;

public class FactorItem {

    private int hourOfDay;
    private float value;

    FactorItem(int hourOfDay, float value) {
        this.hourOfDay = hourOfDay;
        this.value = value;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
