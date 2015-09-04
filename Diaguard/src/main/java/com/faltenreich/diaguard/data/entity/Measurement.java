package com.faltenreich.diaguard.data.entity;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Filip on 11.05.2015.
 */
public abstract class Measurement extends Model {

    public static final String ENTRY_ID = "entry_id";

    public enum Category {
        BloodSugar,
        Insulin,
        Meal,
        Activity,
        HbA1c,
        Weight,
        Pulse,
        Pressure
    }

    @DatabaseField(foreign = true)
    private Entry entry;

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public abstract Category getMeasurementType();
}
