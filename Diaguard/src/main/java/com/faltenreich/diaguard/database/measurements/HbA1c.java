package com.faltenreich.diaguard.database.measurements;

import com.faltenreich.diaguard.database.DatabaseHelper;

/**
 * Created by Filip on 11.05.2015.
 */
public class HbA1c extends Measurement {

    private long percent;

    public long getPercent() {
        return percent;
    }

    public void setPercent(long percent) {
        this.percent = percent;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.HBA1C;
    }
}
