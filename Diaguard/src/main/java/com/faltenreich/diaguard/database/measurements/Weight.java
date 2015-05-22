package com.faltenreich.diaguard.database.measurements;

import com.faltenreich.diaguard.database.DatabaseHelper;

/**
 * Created by Filip on 11.05.2015.
 */
public class Weight extends Measurement {

    private float kilogram;

    public float getKilogram() {
        return kilogram;
    }

    public void setKilogram(float kilogram) {
        this.kilogram = kilogram;
    }

    @Override
    public float getValue() {
        return this.kilogram;
    }

    @Override
    public void setValue(float value) {
        this.kilogram = value;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.WEIGHT;
    }
}
