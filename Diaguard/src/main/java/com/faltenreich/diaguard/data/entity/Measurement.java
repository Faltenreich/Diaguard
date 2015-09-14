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
        BloodSugar,
        Insulin,
        Meal,
        Activity,
        HbA1c,
        Weight,
        Pulse,
        Pressure;

        public Class toClass() {
            switch (this) {
                case BloodSugar:
                    return BloodSugar.getClass();
                case Insulin:
                    return Insulin.getClass();
                case Meal:
                    return Meal.getClass();
                case Activity:
                    return Activity.getClass();
                case HbA1c:
                    return HbA1c.getClass();
                case Weight:
                    return Weight.getClass();
                case Pulse:
                    return Pulse.getClass();
                case Pressure:
                    return Pressure.getClass();
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
}
