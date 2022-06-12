package com.faltenreich.diaguard.shared.data.repository;


import androidx.annotation.NonNull;

import com.faltenreich.diaguard.feature.timeline.table.CategoryValueListItem;
import com.faltenreich.diaguard.shared.data.database.Database;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class EntryRepository {

    private static EntryRepository instance;

    public static EntryRepository getInstance() {
        if (instance == null) {
            instance = new EntryRepository();
        }
        return instance;
    }

    private final EntryDao dao = Database.getInstance().getDatabase().entryDao();

    public Entry createOrUpdate(Entry entry) {
        long id = dao.createOrUpdate(entry);
        entry.setId(id);
        return entry;
    }

    public Entry getById(long id) {
        return dao.getById(id);
    }

    public List<Entry> getAll() {
        return dao.getAll();
    }

    public List<Entry> getBetween(Interval interval) {
        return dao.getBetween(
            interval.getStart().withTimeAtStartOfDay(),
            interval.getEnd().withTime(DateTimeConstants.HOURS_PER_DAY - 1,
                DateTimeConstants.MINUTES_PER_HOUR - 1,
                DateTimeConstants.SECONDS_PER_MINUTE - 1,
                DateTimeConstants.MILLIS_PER_SECOND - 1
            )
        );
    }

    public List<Entry> getByDay(DateTime day) {
        return dao.getBetween(day, day);
    }

    public List<Entry> search(@NonNull String query, int page, int pageSize) {
        // TODO: Implementation
        return new ArrayList<>();
    }

    public LinkedHashMap<Category, CategoryValueListItem[]> getAverageDataTable(DateTime day, Category[] categories, int hoursToSkip) {
        // TODO: Implementation
        return new LinkedHashMap<>();
    }

    public long countBetween(Interval interval) {
        return dao.countBetween(interval.getStart(), interval.getEnd());
    }

    public long countBetween(Interval interval, Category category) {
        return dao.countBetween(interval.getStart(), interval.getEnd(), category);
    }

    public long countBetween(Interval interval, float minimumValue, float maximumValue) {
        return dao.countBetween(interval.getStart(), interval.getEnd(), minimumValue, maximumValue);
    }

    public long countBelow(Interval interval, float maximumValue) {
        return dao.countBelow(interval.getStart(), interval.getEnd(), maximumValue);
    }

    public long countAbove(Interval interval, float minimumValue) {
        return dao.countAbove(interval.getStart(), interval.getEnd(), minimumValue);
    }

    public void delete(Entry entry) {
        dao.delete(entry);
    }

    // TODO: Extract into MeasurementRepository when available

    @Deprecated
    public <M extends Measurement> List<Entry> getAllWithMeasurementFromToday(Class<M> clazz) {
        return new ArrayList<>();
    }

    @Deprecated
    public <M extends Measurement> Entry getLatestWithMeasurement(Class<M> clazz) {
        return null;
    }

    @Deprecated
    public List<Measurement> getMeasurements(Entry entry) {
        return new ArrayList<>();
    }

    @Deprecated
    public List<Measurement> getMeasurements(Entry entry, Category[] categories) {
        return new ArrayList<>();
    }
}
