package com.faltenreich.diaguard.shared.data.database.dao;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;

import org.joda.time.DateTime;

import java.util.List;

public interface EntryDao {

    long createOrUpdate(Entry entry);

    List<Entry> getAll();

    List<Entry> getBetween(DateTime start, DateTime end);

    @Nullable
    Entry getById(long id);

    long countBetween(DateTime start, DateTime end, Category category);

    long countBetween(DateTime start, DateTime end);

    long countBetween(DateTime start, DateTime end, float minimumValue, float maximumValue);

    long countBelow(DateTime start, DateTime end, float maximumValue);

    long countAbove(DateTime start, DateTime end, float minimumValue);

    void delete(Entry entry);
}