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
    public String getTableName() {
        return DatabaseHelper.BLOODSUGAR;
    }

    public Category getMeasurementType() {
        return Category.BloodSugar;
    }
}
