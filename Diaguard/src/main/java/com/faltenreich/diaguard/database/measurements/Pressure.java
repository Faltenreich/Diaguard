package com.faltenreich.diaguard.database.measurements;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Filip on 11.05.2015.
 */
@DatabaseTable
public class Pressure extends Measurement {

    public static final String SYSTOLIC = "systolic";
    public static final String DIASTOLIC = "diastolic";

    @DatabaseField
    private float systolic;

    @DatabaseField
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

    public Category getMeasurementType() {
        return Category.Pressure;
    }
}
