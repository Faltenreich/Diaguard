package com.faltenreich.diaguard.database;

import com.faltenreich.diaguard.helpers.Helper;
import com.faltenreich.diaguard.database.measurements.Measurement;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by Filip on 09.08.2014.
 */
public class Entry extends Model {

    private DateTime date;
    private String note;
    private boolean isVisible;
    private ArrayList<Measurement> measurements;

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

    public ArrayList<Measurement> getMeasurements() {
        if(measurements == null) {
            measurements = new ArrayList<>();
        }
        return measurements;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.ENTRY;
    }
}
