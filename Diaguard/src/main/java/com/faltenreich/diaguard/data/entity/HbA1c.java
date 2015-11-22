package com.faltenreich.diaguard.data.entity;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Filip on 11.05.2015.
 */
@DatabaseTable
public class HbA1c extends Measurement {

    public class Column extends Measurement.Column {
        public static final String PERCENT = "percent";
    }

    @DatabaseField(columnName = Column.PERCENT)
    private float percent;

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    @Override
    public Category getCategory() {
        return Category.HBA1C;
    }

    @Override
    public float[] getValues() {
        return new float[] { percent };
    }

    @Override
    public void setValues(float... values) {
        if (values.length > 0) {
            percent = values[0];
        }
    }

    @Override
    public String toString() {
        return PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), percent);
    }
}
