package com.faltenreich.diaguard.data.entity;

import android.support.annotation.StringRes;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Filip on 11.05.2015.
 */
public abstract class Measurement extends BaseEntity {

    public class Column extends BaseEntity.Column {
        public static final String ENTRY = "entry";
    }

    public enum Category {
        BLOODSUGAR(BloodSugar.class, R.string.bloodsugar, false),
        INSULIN(Insulin.class, R.string.insulin, true),
        MEAL(Meal.class, R.string.meal, true),
        ACTIVITY(Activity.class, R.string.activity, true),
        HBA1C(HbA1c.class, R.string.hba1c, false),
        WEIGHT(Weight.class, R.string.weight, false),
        PULSE(Pulse.class, R.string.pulse, false),
        PRESSURE(Pressure.class, R.string.pressure, false);

        private Class clazz;
        private int stringResId;
        private boolean stackValues;

        Category(Class clazz, @StringRes int stringResId, boolean stackValues) {
            this.clazz = clazz;
            this.stringResId = stringResId;
            this.stackValues = stackValues;
        }

        public Class toClass() {
            return clazz;
        }

        public String toLocalizedString() {
            return DiaguardApplication.getContext().getString(stringResId);
        }

        public boolean stackValues() {
            return stackValues;
        }
    }

    @DatabaseField(columnName = Column.ENTRY, foreign = true, foreignAutoRefresh = true)
    private Entry entry;

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public abstract Category getCategory();

    public String[] getValuesForUI() {
        float[] values = getValues();
        String[] valuesForUI = new String[values.length];
        for (int position = 0; position < values.length; position++) {
            float value = values[position];
            if (value > 0) {
                float valueFormatted = PreferenceHelper.getInstance().formatDefaultToCustomUnit(getCategory(), value);
                valuesForUI[position] = PreferenceHelper.getInstance().getDecimalFormat(getCategory()).format(valueFormatted);
            }
        }
        return valuesForUI;
    }

    public String getValueForUI() {
        float sum = 0;
        for (float value : getValues()) {
            sum += PreferenceHelper.getInstance().formatDefaultToCustomUnit(getCategory(), value);
        }
        return PreferenceHelper.getInstance().getDecimalFormat(getCategory()).format(sum);
    }

    @SuppressWarnings("unchecked")
    public abstract float[] getValues();

    @SuppressWarnings("unchecked")
    public abstract void setValues(float... values);
}
