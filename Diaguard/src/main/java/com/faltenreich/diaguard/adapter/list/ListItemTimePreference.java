package com.faltenreich.diaguard.adapter.list;

import com.faltenreich.diaguard.data.TimeInterval;

/**
 * Created by Faltenreich on 03.09.2016.
 */
public class ListItemTimePreference extends ListItem {

    public enum Type {
        BLOOD_SUGAR,
        FACTOR
    }

    private Type type;
    private TimeInterval interval;
    private int hourOfDay;
    private float value;

    public ListItemTimePreference(Type type, TimeInterval interval, int hourOfDay, float value) {
        this.type = type;
        this.interval = interval;
        this.hourOfDay = hourOfDay;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public TimeInterval getInterval() {
        return interval;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public float getValue() {
        return value;
    }
}
