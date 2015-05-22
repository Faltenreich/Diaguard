package com.faltenreich.diaguard.database.measurements;

import com.faltenreich.diaguard.database.DatabaseHelper;

/**
 * Created by Filip on 11.05.2015.
 */
public class Pulse extends Measurement {

    private float frequency;

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    @Override
    public float getValue() {
        return this.frequency;
    }

    @Override
    public void setValue(float value) {
        this.frequency = value;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.PULSE;
    }
}
