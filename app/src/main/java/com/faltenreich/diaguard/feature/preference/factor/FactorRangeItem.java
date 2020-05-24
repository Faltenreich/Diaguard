package com.faltenreich.diaguard.feature.preference.factor;

class FactorRangeItem {

    private int hourOfDay;
    private int rangeInHours;
    private float value;

    FactorRangeItem(int hourOfDay, int rangeInHours, float value) {
        this.hourOfDay = hourOfDay;
        this.rangeInHours = rangeInHours;
        this.value = value;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public int getRangeInHours() {
        return rangeInHours;
    }

    public void setRangeInHours(int rangeInHours) {
        this.rangeInHours = rangeInHours;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
