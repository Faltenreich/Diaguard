package com.faltenreich.diaguard.database.measurements;

import com.faltenreich.diaguard.database.Model;

/**
 * Created by Filip on 11.05.2015.
 */
public abstract class Measurement extends Model {

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

    private long entryId;

    public long getEntryId() {
        return entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }

    public abstract Category getMeasurementType();
}
