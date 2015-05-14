package com.faltenreich.diaguard.database;

/**
 * Created by Filip on 20.10.13.
 */
public class Measurement extends Model {

    private float value;
    private Category category;
    private long entryId;

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

    @Override
    public String getTableName() {
        return DatabaseHelper.MEASUREMENT;
    }
}
