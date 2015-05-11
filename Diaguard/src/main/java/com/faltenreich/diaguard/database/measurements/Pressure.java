package com.faltenreich.diaguard.database.measurements;

import com.faltenreich.diaguard.database.DatabaseHelper;

/**
 * Created by Filip on 11.05.2015.
 */
public class Pressure extends Measurement {

    private long systolic;
    private long diastolic;

    public long getSystolic() {
        return systolic;
    }

    public void setSystolic(long systolic) {
        this.systolic = systolic;
    }

    public long getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(long diastolic) {
        this.diastolic = diastolic;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.PRESSURE;
    }
}
