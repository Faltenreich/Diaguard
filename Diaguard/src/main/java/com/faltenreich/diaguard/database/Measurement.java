package com.faltenreich.diaguard.database;

import com.faltenreich.diaguard.helpers.Helper;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by Filip on 20.10.13.
 */
public class Measurement {

    private long id;
    private float value;
    private Category category;
    private long entryId;

    public enum Category {
        BloodSugar,
        Bolus,
        Meal,
        Activity,
        HbA1c,
        Weight,
        Pulse
    }

    public long getId() { return id; }

    public void setId(long id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Category getCategory() { return category; }

    public void setCategory(Category category) { this.category = category; }

    public long getEntryId() {
        return entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }
}
