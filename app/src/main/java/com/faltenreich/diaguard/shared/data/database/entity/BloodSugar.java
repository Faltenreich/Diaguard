package com.faltenreich.diaguard.shared.data.database.entity;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.feature.preference.data.PreferenceHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class BloodSugar extends Measurement {

    public class Column extends Measurement.Column {
        public static final String MGDL = "mgDl";
    }

    @DatabaseField(columnName = Column.MGDL)
    private float mgDl;

    public float getMgDl() {
        return mgDl;
    }

    public void setMgDl(float mgDl) {
        this.mgDl = mgDl;
    }

    @Override
    public Category getCategory() {
        return Category.BLOODSUGAR;
    }

    @Override
    public float[] getValues() {
        return new float[] { mgDl };
    }

    @Override
    public void setValues(float... values) {
        if (values.length > 0) {
            mgDl = values[0];
        }
    }

    @NonNull
    @Override
    public String toString() {
        return PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), mgDl);
    }
}
