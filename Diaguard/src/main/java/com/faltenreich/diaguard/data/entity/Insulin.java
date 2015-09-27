package com.faltenreich.diaguard.data.entity;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Filip on 11.05.2015.
 */
@DatabaseTable
public class Insulin extends Measurement {

    public static final String BOLUS = "bolus";
    public static final String CORRECTION = "correction";
    public static final String BASAL = "basal";

    @DatabaseField
    private float bolus;

    @DatabaseField
    private float correction;

    @DatabaseField
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
    public Category getCategory() {
        return Category.INSULIN;
    }

    @Override
    public float[] getValues() {
        return new float[] { bolus, correction, basal };
    }

    @Override
    public void setValues(float... values) {
        bolus = values[0];
        correction = values[1];
        basal = values[2];
    }

    @Override
    public String toString() {
        float total = bolus + correction + basal;
        StringBuilder stringBuilder = new StringBuilder(PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), total));
        stringBuilder.append(" (");
        boolean isFirstValue = true;
        if (getBolus() > 0) {
            stringBuilder.append(String.format("%s %s",
                    PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), bolus),
                    DiaguardApplication.getContext().getString(R.string.bolus)));
            isFirstValue = false;
        }
        if (getCorrection() > 0) {
            if (!isFirstValue) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(String.format("%s %s",
                    PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), correction),
                    DiaguardApplication.getContext().getString(R.string.correction)));
            isFirstValue = false;
        }
        if (getBasal() > 0) {
            if (!isFirstValue) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(String.format("%s %s",
                    PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), basal),
                    DiaguardApplication.getContext().getString(R.string.basal)));
        }
        stringBuilder.append(")");
        return stringBuilder.toString().trim();
    }
}
