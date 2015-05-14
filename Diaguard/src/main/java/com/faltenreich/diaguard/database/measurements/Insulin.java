package com.faltenreich.diaguard.database.measurements;

import com.faltenreich.diaguard.database.DatabaseHelper;

/**
 * Created by Filip on 11.05.2015.
 */
public class Insulin extends Measurement {

    private long bolus;
    private long correction;
    private long basal;

    public long getBolus() {
        return bolus;
    }

    public void setBolus(long bolus) {
        this.bolus = bolus;
    }

    public long getCorrection() {
        return correction;
    }

    public void setCorrection(long correction) {
        this.correction = correction;
    }

    public long getBasal() {
        return basal;
    }

    public void setBasal(long basal) {
        this.basal = basal;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.INSULIN;
    }
}
