package com.faltenreich.diaguard.database.measurements;

import com.faltenreich.diaguard.database.DatabaseHelper;

/**
 * Created by Filip on 11.05.2015.
 */
public class Pressure extends Measurement {

    private float systolic;
    private float diastolic;

    public float getSystolic() {
        return systolic;
    }

    public void setSystolic(float systolic) {
        this.systolic = systolic;
    }

    public float getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(float diastolic) {
        this.diastolic = diastolic;
    }

    @Override
    public void setValue(float value) {
        this.systolic = value;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.PRESSURE;
    }
}
