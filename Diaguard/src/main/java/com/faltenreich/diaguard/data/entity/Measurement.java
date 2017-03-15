package com.faltenreich.diaguard.data.entity;

import android.support.annotation.StringRes;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.util.Helper;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Filip on 11.05.2015.
 */
public abstract class Measurement extends BaseEntity {

    public class Column extends BaseEntity.Column {
        public static final String ENTRY = "entry";
    }

    public enum Category {
        BLOODSUGAR(BloodSugar.class, 1, R.string.bloodsugar, false),
        INSULIN(Insulin.class, 2, R.string.insulin, true),
        MEAL(Meal.class, 3, R.string.meal, true),
        ACTIVITY(Activity.class, 4, R.string.activity, true),
        HBA1C(HbA1c.class, 5, R.string.hba1c, false),
        WEIGHT(Weight.class, 6, R.string.weight, false),
        PULSE(Pulse.class, 7, R.string.pulse, false),
        PRESSURE(Pressure.class, 8, R.string.pressure, false),
        OXYGEN_SATURATION(OxygenSaturation.class, 9, R.string.oxygen_saturation, false);

        private Class clazz;
        private int maskId;
        private int stringResId;
        private boolean stackValues;

        Category(Class clazz, int maskId, @StringRes int stringResId, boolean stackValues) {
            this.clazz = clazz;
            this.maskId = maskId;
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

        public int getMaskId() {
            return maskId;
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
                valuesForUI[position] = Helper.parseFloat(valueFormatted);
            }
        }
        return valuesForUI;
    }

    @SuppressWarnings("unchecked")
    public abstract float[] getValues();

    @SuppressWarnings("unchecked")
    public abstract void setValues(float... values);
}
