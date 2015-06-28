package com.faltenreich.diaguard.database;

import com.faltenreich.diaguard.database.measurements.Measurement;
import com.faltenreich.diaguard.helpers.Helper;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.DateTime;

/**
 * Created by Filip on 09.08.2014.
 */
@DatabaseTable
public class Entry extends Model {

    public static final String DATE = "date";
    public static final String NOTE = "note";
    public static final String IS_VISIBLE = "isvisible";

    @DatabaseField(dataType = DataType.DATE_TIME)
    private DateTime date;

    @DatabaseField(dataType = DataType.STRING)
    private String note;

    @DatabaseField(dataType = DataType.BOOLEAN)
    private boolean isVisible;

    @ForeignCollectionField
    private ForeignCollection<Measurement> measurements;

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public void setDate(String dateString) {
        this.date = new DateTime(Helper.getDateDatabaseFormat().parseDateTime(dateString));
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public ForeignCollection<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(ForeignCollection<Measurement> measurements) {
        this.measurements = measurements;
    }
}
