package com.faltenreich.diaguard.shared.data.database.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class BloodSugar extends Measurement {

    public class Column extends Measurement.Column {
        public static final String MGDL = "mgDl";
        public static final String TREND = "trend";
    }

    public enum Trend {
        FALLING_QUICKLY,
        FALLING,
        STEADY,
        RISING,
        RISING_QUICKLY
    }

    @DatabaseField(columnName = Column.MGDL)
    private float mgDl;

    @DatabaseField(columnName = Column.TREND, dataType = DataType.ENUM_STRING)
    private Trend trend;

    public float getMgDl() {
        return mgDl;
    }

    public void setMgDl(float mgDl) {
        this.mgDl = mgDl;
    }

    @Nullable
    public Trend getTrend() {
        return trend;
    }

    public void setTrend(@Nullable Trend trend) {
        this.trend = trend;
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
        return PreferenceStore.getInstance().getMeasurementForUi(getCategory(), mgDl);
    }
}
