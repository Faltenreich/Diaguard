package com.faltenreich.diaguard.data.entity;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Filip on 09.08.2014.
 */
@DatabaseTable
public class BloodSugar extends Measurement {

    public static final String MGDL = "mgDl";

    @DatabaseField
    private float mgDl;

    public float getMgDl() {
        return mgDl;
    }

    public void setMgDl(float mgDl) {
        this.mgDl = mgDl;
    }

    @Override
    public Category getMeasurementType() {
        return Category.BLOODSUGAR;
    }

    @Override
    public void setValues(float... values) {
        mgDl = values[0];
    }

    @Override
    public String toString() {
        return PreferenceHelper.getInstance().getMeasurementForUi(getMeasurementType(), mgDl);
    }
}
