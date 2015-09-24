package com.faltenreich.diaguard.data.entity;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Filip on 11.05.2015.
 */
public abstract class Measurement extends BaseEntity {

    public static final String ENTRY_ID = "entry_id";

    public enum Category {
        BLOODSUGAR,
        INSULIN,
        MEAL,
        ACTIVITY,
        HBA1C,
        WEIGHT,
        PULSE,
        PRESSURE;

        public Class toClass() {
            switch (this) {
                case BLOODSUGAR:
                    return BloodSugar.class;
                case INSULIN:
                    return Insulin.class;
                case MEAL:
                    return Meal.class;
                case ACTIVITY:
                    return Activity.class;
                case HBA1C:
                    return HbA1c.class;
                case WEIGHT:
                    return Weight.class;
                case PULSE:
                    return Pulse.class;
                case PRESSURE:
                    return Pressure.class;
                default:
                    throw new IllegalArgumentException("Class is not supported");
            }
        }

        @Override
        public String toString() {
            switch (this) {
                case BLOODSUGAR:
                    return DiaguardApplication.getContext().getString(R.string.bloodsugar);
                case INSULIN:
                    return DiaguardApplication.getContext().getString(R.string.insulin);
                case MEAL:
                    return DiaguardApplication.getContext().getString(R.string.meal);
                case ACTIVITY:
                    return DiaguardApplication.getContext().getString(R.string.activity);
                case HBA1C:
                    return DiaguardApplication.getContext().getString(R.string.hba1c);
                case WEIGHT:
                    return DiaguardApplication.getContext().getString(R.string.weight);
                case PULSE:
                    return DiaguardApplication.getContext().getString(R.string.pulse);
                case PRESSURE:
                    return DiaguardApplication.getContext().getString(R.string.pressure);
                default:
                    return null;
            }
        }
    }

    @DatabaseField(foreign = true)
    private Entry entry;

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public abstract Category getMeasurementType();

    @SuppressWarnings("unchecked")
    public abstract void setValues(float... values);

}
