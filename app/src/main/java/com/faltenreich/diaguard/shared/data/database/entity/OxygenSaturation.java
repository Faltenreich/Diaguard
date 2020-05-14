package com.faltenreich.diaguard.shared.data.database.entity;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class OxygenSaturation extends Measurement {

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
        return Category.OXYGEN_SATURATION;
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

    @NonNull
    @Override
    public String toString() {
        return PreferenceStore.getInstance().getMeasurementForUi(getCategory(), percent);
    }
}
