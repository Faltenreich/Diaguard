package com.faltenreich.diaguard.data.entity;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Pulse extends Measurement {

    public class Column extends Measurement.Column {
        public static final String FREQUENCY = "frequency";
    }

    @DatabaseField
    private float frequency;

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    @Override
    public Category getCategory() {
        return Category.PULSE;
    }

    @Override
    public float[] getValues() {
        return new float[] { frequency };
    }

    @Override
    public void setValues(float... values) {
        if (values.length > 0) {
            frequency = values[0];
        }
    }

    @Override
    public String toString() {
        return PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), frequency);
    }
}
