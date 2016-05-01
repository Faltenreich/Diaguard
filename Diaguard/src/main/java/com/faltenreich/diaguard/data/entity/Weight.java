package com.faltenreich.diaguard.data.entity;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Filip on 11.05.2015.
 */
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

    @Override
    public String toString() {
        return PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), kilogram);
    }
}
