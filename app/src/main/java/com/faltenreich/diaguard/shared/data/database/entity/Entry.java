package com.faltenreich.diaguard.shared.data.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.faltenreich.diaguard.feature.export.Backupable;
import com.faltenreich.diaguard.feature.export.Exportable;
import com.faltenreich.diaguard.feature.export.job.Export;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

@Entity
public class Entry extends BaseEntity implements Backupable, Exportable {

    public static final String BACKUP_KEY = "entry";

    public class Column extends BaseEntity.Column {
        public static final String DATE = "date";
        public static final String NOTE = "note";
    }

    @ColumnInfo(name = Column.DATE)
    private DateTime date;

    @ColumnInfo(name = Column.NOTE)
    private String note;

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

    public List<Measurement> getMeasurementCache() {
        return EntryDao.getInstance().getMeasurements(this);
    }

    public int indexInMeasurementCache(Category category) {
        List<Measurement> measurements = getMeasurementCache();
        if (measurements != null) {
            for (int index = 0; index < measurements.size(); index++) {
                Measurement measurement = measurements.get(index);
                if (measurement.getCategory() == category) {
                    return index;
                }
            }
        }
        return -1;
    }

    public void setMeasurementCache(List<Measurement> measurementCache) {
        for (Measurement measurement : measurementCache) {
            MeasurementDao.getInstance(measurement.getClass()).createOrUpdate(measurement);
        }
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
