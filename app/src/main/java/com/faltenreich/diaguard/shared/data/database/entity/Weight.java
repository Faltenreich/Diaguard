package com.faltenreich.diaguard.shared.data.database.entity;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.feature.preference.PreferenceHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Weight extends Measurement {

    public class Column extends Measurement.Column {
        public static final String KILOGRAM = "kilogram";
    }

    @DatabaseField(columnName = Column.KILOGRAM)
    private float kilogram;

    public float getKilogram() {
        return kilogram;
    }

    public void setKilogram(float kilogram) {
        this.kilogram = kilogram;
    }

    @Override
    public Category getCategory() {
        return Category.WEIGHT;
    }

    @Override
    public float[] getValues() {
        return new float[] { kilogram };
    }

    @Override
    public void setValues(float... values) {
        if (values.length > 0) {
            kilogram = values[0];
        }
    }

    @NonNull
    @Override
    public String toString() {
        return PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), kilogram);
    }
}
