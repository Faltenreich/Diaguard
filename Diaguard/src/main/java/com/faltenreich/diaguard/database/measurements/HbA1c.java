package com.faltenreich.diaguard.database.measurements;

import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Filip on 11.05.2015.
 */
@DatabaseTable
public class HbA1c extends Measurement {

    public static final String PERCENT = "percent";

    @DatabaseField
    private float percent;

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public Category getMeasurementType() {
        return Category.HbA1c;
    }

    @Override
    public String toString() {
        return PreferenceHelper.getInstance().getMeasurementForUi(getMeasurementType(), percent);
    }
}
