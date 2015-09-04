package com.faltenreich.diaguard.data.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 09.08.2014.
 */
@DatabaseTable
public class Entry extends Model {

    public static final String DATE = "date";
    public static final String NOTE = "note";
    public static final String IS_VISIBLE = "isvisible";

    @DatabaseField
    private DateTime date;

    @DatabaseField
    private String note;

    @DatabaseField
    private boolean isVisible;

    @ForeignCollectionField
    private ForeignCollection<Measurement> measurements;

    private List<Measurement> measurementCache;

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
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

    public List<Measurement> getMeasurementCache() {
        if (measurementCache == null) {
            measurementCache = new ArrayList<>();
        }
        return measurementCache;
    }

    public void setMeasurementCache(List<Measurement> measurementCache) {
        this.measurementCache = measurementCache;
    }
}
