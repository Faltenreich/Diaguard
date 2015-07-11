package com.faltenreich.diaguard.database.measurements;

import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Filip on 11.05.2015.
 */
@DatabaseTable
public class Pulse extends Measurement {

    public static final String FREQUENCY = "frequency";

    @DatabaseField
    private float frequency;

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    public Category getMeasurementType() {
        return Category.Pulse;
    }

    @Override
    public String toString() {
        return PreferenceHelper.getInstance().getMeasurementForUi(getMeasurementType(), frequency);
    }
}
