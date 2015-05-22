package com.faltenreich.diaguard.database.measurements;

import com.faltenreich.diaguard.database.DatabaseHelper;

/**
 * Created by Filip on 11.05.2015.
 */
public class Activity extends Measurement {

    private int minutes;
    private int type;

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public float getValue() {
        return this.minutes;
    }

    @Override
    public void setValue(float value) {
        this.minutes = (int) value;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.ACTIVITY;
    }
}
