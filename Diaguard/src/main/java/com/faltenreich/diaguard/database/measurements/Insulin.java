package com.faltenreich.diaguard.database.measurements;

import com.faltenreich.diaguard.database.DatabaseHelper;

/**
 * Created by Filip on 11.05.2015.
 */

public class Insulin extends Measurement {

    private float bolus;
    private float correction;
    private float basal;

    public float getBolus() {
        return bolus;
    }

    public void setBolus(float bolus) {
        this.bolus = bolus;
    }

    public float getCorrection() {
        return correction;
    }

    public void setCorrection(float correction) {
        this.correction = correction;
    }

    public float getBasal() {
        return basal;
    }

    public void setBasal(float basal) {
        this.basal = basal;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.INSULIN;
    }
}
