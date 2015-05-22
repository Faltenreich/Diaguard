package com.faltenreich.diaguard.database.measurements;

import com.faltenreich.diaguard.database.DatabaseHelper;

/**
 * Created by Filip on 09.08.2014.
 */
public class BloodSugar extends Measurement {

    private float mgDl;

    public float getMgDl() {
        return mgDl;
    }

    public void setMgDl(float mgDl) {
        this.mgDl = mgDl;
    }

    @Override
    public float getValue() {
        return this.mgDl;
    }

    @Override
    public void setValue(float value) {
        this.mgDl = value;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.BLOODSUGAR;
    }
}
