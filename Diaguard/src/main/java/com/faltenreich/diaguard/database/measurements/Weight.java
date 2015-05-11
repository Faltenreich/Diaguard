package com.faltenreich.diaguard.database.measurements;

import com.faltenreich.diaguard.database.DatabaseHelper;

/**
 * Created by Filip on 11.05.2015.
 */
public class Weight extends Measurement {

    private long kilogram;

    public long getKilogram() {
        return kilogram;
    }

    public void setKilogram(long kilogram) {
        this.kilogram = kilogram;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.WEIGHT;
    }
}
