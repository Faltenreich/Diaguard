package com.faltenreich.diaguard.database.measurements;

import com.faltenreich.diaguard.database.DatabaseHelper;

/**
 * Created by Filip on 11.05.2015.
 */
public class Bolus extends Measurement {

    private long milliliter;
    private int atcCode;
    private boolean isCorrection;

    public long getMilliliter() {
        return milliliter;
    }

    public void setMilliliter(long milliliter) {
        this.milliliter = milliliter;
    }

    public int getAtcCode() {
        return atcCode;
    }

    public void setAtcCode(int atcCode) {
        this.atcCode = atcCode;
    }

    public boolean isCorrection() {
        return isCorrection;
    }

    public void setIsCorrection(boolean isCorrection) {
        this.isCorrection = isCorrection;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.BOLUS;
    }
}
