package com.faltenreich.diaguard.shared.data.database.entity;

import com.faltenreich.diaguard.feature.export.Backupable;
import com.faltenreich.diaguard.feature.export.Exportable;
import com.faltenreich.diaguard.feature.export.job.Export;
import com.faltenreich.diaguard.shared.Helper;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable
public class Entry extends BaseEntity implements Backupable, Exportable {

    public static final String BACKUP_KEY = "entry";

    public class Column extends BaseEntity.Column {
        public static final String DATE = "date";
        public static final String NOTE = "note";
        public static final String SOURCE = "source";
    }

    public enum Source {
        USER,
        CGM
    }

    @DatabaseField(columnName = Column.DATE)
    private DateTime date;

    @DatabaseField(columnName = Column.NOTE)
    private String note;

    @DatabaseField(
        columnName = Column.SOURCE,
        dataType = DataType.ENUM_STRING,
        defaultValue = "USER",
        unknownEnumName = "USER"
    )
    private Source source;

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

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
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

    public int indexInMeasurementCache(Category category) {
        if (measurementCache != null) {
            for (int index = 0; index < measurementCache.size(); index++) {
                Measurement measurement = measurementCache.get(index);
                if (measurement.getCategory() == category) {
                    return index;
                }
            }
        }
        return -1;
    }

    public void setMeasurementCache(List<Measurement> measurementCache) {
        this.measurementCache = measurementCache;
    }

    @Override
    public String getKeyForBackup() {
        return BACKUP_KEY;
    }

    @Override
    public String[] getValuesForBackup() {
        return new String[]{DateTimeFormat.forPattern(Export.BACKUP_DATE_FORMAT).print(date), note};
    }

    @Override
    public String[] getValuesForExport() {
        return new String[] {
            String.format("%s %s",
                Helper.getDateFormat().print(date),
                Helper.getTimeFormat().print(date)
            ).toLowerCase(),
            note
        };
    }
}
