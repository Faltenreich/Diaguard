package com.faltenreich.diaguard.shared.data.repository;


import androidx.annotation.NonNull;

import com.faltenreich.diaguard.feature.timeline.table.CategoryValueListItem;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.j256.ormlite.stmt.QueryBuilder;

import org.joda.time.DateTime;
import org.joda.time.Interval;

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

    private final EntryDao dao = EntryDao.getInstance();

    public Entry createOrUpdate(Entry entry) {
        return dao.createOrUpdate(entry);
    }

    public List<Entry> getAll() {
        return dao.getAll();
    }

    public List<Entry> getByDay(DateTime day) {
        return dao.getEntriesOfDay(day);
    }

    public List<Entry> getBetween(Interval interval) {
        return dao.getEntriesBetween(interval.getStart(), interval.getEnd());
    }

    public Entry getById(long id) {
        return dao.getById(id);
    }

    public List<Entry> search(@NonNull String query, int page, int pageSize) {
        return dao.search(query, page, pageSize);
    }

    public LinkedHashMap<Category, CategoryValueListItem[]> getAverageDataTable(DateTime day, Category[] categories, int hoursToSkip) {
        return dao.getAverageDataTable(day, categories, hoursToSkip);
    }

    public long countBetween(Interval interval, Category category) {
        return dao.count(category, interval.getStart(), interval.getEnd());
    }

    public long countBetween(Interval interval) {
        return dao.count(interval.getStart(), interval.getEnd());
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

    // TODO: Extract into MeasurementRepository

    public QueryBuilder<Entry, Long> getQueryBuilder() {
        return dao.getQueryBuilder();
    }

    public <M extends Measurement> List<Entry> getAllWithMeasurementFromToday(Class<M> clazz) {
        return dao.getAllWithMeasurementFromToday(clazz);
    }

    public <M extends Measurement> Entry getLatestWithMeasurement(Class<M> clazz) {
        return dao.getLatestWithMeasurement(clazz);
    }

    public List<Measurement> getMeasurements(Entry entry) {
        return dao.getMeasurements(entry);
    }

    public List<Measurement> getMeasurements(Entry entry, Category[] categories) {
        return dao.getMeasurements(entry, categories);
    }
}
