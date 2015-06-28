package com.faltenreich.diaguard.database.measurements;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Filip on 11.05.2015.
 */
@DatabaseTable
public class Weight extends Measurement {

    public static final String KILOGRAM = "kilogram";

    @DatabaseField
    private float kilogram;

    public float getKilogram() {
        return kilogram;
    }

    public void setKilogram(float kilogram) {
        this.kilogram = kilogram;
    }

    public Category getMeasurementType() {
        return Category.Weight;
    }
}
