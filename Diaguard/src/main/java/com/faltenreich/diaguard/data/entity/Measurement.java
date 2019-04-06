package com.faltenreich.diaguard.data.entity;

import android.content.Context;
import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.Backupable;
import com.faltenreich.diaguard.data.Exportable;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.util.Helper;
import com.j256.ormlite.field.DatabaseField;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public abstract class Measurement extends BaseEntity implements Backupable, Exportable {

    public static final String BACKUP_KEY = "measurement";

    private static final String TAG = Measurement.class.getSimpleName();

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
        private int stringResId;
        private boolean stackValues;
        private int stableId;

        Category(Class clazz, int stableId, @StringRes int stringResId, boolean stackValues) {
            this.clazz = clazz;
            this.stableId = stableId;
            this.stringResId = stringResId;
            this.stackValues = stackValues;
        }

        public <M extends Measurement> M createMeasurement() {
            switch (this) {
                case BLOODSUGAR:
                    return (M) new BloodSugar();
                case INSULIN:
                    return (M) new Insulin();
                case MEAL:
                    return (M) new Meal();
                case ACTIVITY:
                    return (M) new Activity();
                case HBA1C:
                    return (M) new HbA1c();
                case WEIGHT:
                    return (M) new Weight();
                case PULSE:
                    return (M) new Pulse();
                case PRESSURE:
                    return (M) new Pressure();
                case OXYGEN_SATURATION:
                    return (M) new OxygenSaturation();
                default:
                    return null;
            }
        }

        public <M extends Measurement> Class<M> toClass() {
            return clazz;
        }

        public String toLocalizedString(Context context) {
            return context.getString(stringResId);
        }

        public boolean stackValues() {
            return stackValues;
        }

        public int getStableId() {
            return stableId;
        }

        public boolean isOptional() {
            return this != BLOODSUGAR;
        }

        public static Category fromStableId(int stableId) {
            for (Category category : values()) {
                if (category.stableId == stableId) {
                    return category;
                }
            }
            return null;
        }

        @Nullable
        public static String serialize(@Nullable Category[] categories) {
            if (categories != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Measurement.Category category : categories) {
                    stringBuilder.append(category.getStableId());
                    stringBuilder.append(';');
                }
                return stringBuilder.toString();
            } else {
                return null;
            }
        }

        @Nullable
        public static Category[] deserialize(@Nullable String string) {
            if (string != null) {
                String[] ids = string.split(";");
                List<Category> categories = new ArrayList<>();
                for (String id : ids) {
                    try {
                        int stableId = Integer.parseInt(id);
                        Category category = Measurement.Category.fromStableId(stableId);
                        if (category != null) {
                            categories.add(category);
                        }
                    } catch (NumberFormatException exception) {
                        Log.e(TAG, exception.getMessage());
                    }
                }
                return categories.toArray(new Category[categories.size()]);
            } else {
                return null;
            }
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
            if (value != 0) {
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

    @Override
    public String getKeyForBackup() {
        return BACKUP_KEY;
    }

    @Override
    public String[] getValuesForBackup() {
        float[] values = getValues();
        String[] valuesForBackup = new String[values.length];
        for (int index = 0; index < values.length; index++) {
            valuesForBackup[index] = Float.toString(values[index]);
        }
        return ArrayUtils.addAll(new String[]{getCategory().name().toLowerCase()}, valuesForBackup);
    }

    @Override
    public String[] getValuesForExport() {
        return ArrayUtils.addAll(new String[]{getCategory().name().toLowerCase()}, getValuesForUI());
    }
}
