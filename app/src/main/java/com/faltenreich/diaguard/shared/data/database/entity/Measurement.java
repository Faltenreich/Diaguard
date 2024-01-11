package com.faltenreich.diaguard.shared.data.database.entity;

import android.content.Context;

import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.feature.export.Backupable;
import com.faltenreich.diaguard.feature.export.Exportable;
import com.j256.ormlite.field.DatabaseField;

import org.apache.commons.lang3.ArrayUtils;

public abstract class Measurement extends BaseEntity implements Backupable, Exportable {

    public static final String BACKUP_KEY = "measurement";

    public class Column extends BaseEntity.Column {
        public static final String ENTRY = "entry";
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
                float valueFormatted = PreferenceStore.getInstance().formatDefaultToCustomUnit(getCategory(), value);
                valuesForUI[position] = FloatUtils.parseFloat(valueFormatted);
            }
        }
        return valuesForUI;
    }

    public abstract float[] getValues();

    public abstract void setValues(float... values);

    public boolean hasValue() {
        for (float value : getValues()) {
            if (value > 0) {
                return true;
            }
        }
        if (getCategory() == Category.MEAL) {
            return !((Meal) this).getFoodEatenCache().isEmpty();
        }
        return false;
    }

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
    public String[] getValuesForExport(Context context) {
        return ArrayUtils.addAll(new String[]{context.getString(getCategory().getStringResId())}, getValuesForUI());
    }

    public String print(Context context) {
        return String.format("%s %s",
            toString(),
            PreferenceStore.getInstance().getUnitAcronym(getCategory())
        );
    }
}
