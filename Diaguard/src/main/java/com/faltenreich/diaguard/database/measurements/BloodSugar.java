package com.faltenreich.diaguard.database.measurements;

import com.faltenreich.diaguard.database.DatabaseHelper;

/**
 * Created by Filip on 09.08.2014.
 */
public class BloodSugar extends Measurement {

    private long mgDl;

    public long getMgDl() {
        return mgDl;
    }

    public void setMgDl(long mgDl) {
        this.mgDl = mgDl;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.BLOODSUGAR;
    }
}
