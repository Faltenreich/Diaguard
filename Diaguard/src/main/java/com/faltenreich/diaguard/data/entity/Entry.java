package com.faltenreich.diaguard.data.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable
public class Entry extends BaseEntity {

    public class Column extends BaseEntity.Column {
        public static final String DATE = "date";
        public static final String NOTE = "note";
    }

    @DatabaseField(columnName = Column.DATE)
    private DateTime date;

    @DatabaseField(columnName = Column.NOTE)
    private String note;

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

    public ForeignCollection<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(ForeignCollection<Measurement> measurements) {
        this.measurements = measurements;
    }

    // TODO: Get rid of measurementCache (Currently required to enable lazy loading for generic measurements
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
