package com.faltenreich.diaguard.data.entity;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Filip on 11.05.2015.
 */
@DatabaseTable
public class Activity extends Measurement {

    public class Column extends Measurement.Column {
        public static final String MINUTES = "minutes";
        public static final String TYPE = "type";
    }

    public enum Type {
        // TODO
    }

    @DatabaseField(columnName = Column.MINUTES)
    private int minutes;

    @DatabaseField(columnName = Column.TYPE)
    private Type type;

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public Category getCategory() {
        return Category.ACTIVITY;
    }

    @Override
    public float[] getValues() {
        return new float[] { minutes };
    }

    @Override
    public void setValues(float... values) throws IndexOutOfBoundsException{
        minutes = (int) values[0];
    }

    @Override
    public String toString() {
        return PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), minutes);
    }
}
