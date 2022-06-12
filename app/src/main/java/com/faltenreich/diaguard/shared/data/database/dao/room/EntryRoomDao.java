package com.faltenreich.diaguard.shared.data.database.dao.room;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;

import org.joda.time.DateTime;

import java.util.List;

@Dao
public interface EntryRoomDao extends EntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createOrUpdate(Entry entry);

    @Query("SELECT * FROM Entry")
    List<Entry> getAll();

    @Query("SELECT * FROM Entry WHERE date >= :start AND date <= :end ORDER BY date ASC")
    List<Entry> getBetween(DateTime start, DateTime end);

    @Nullable
    @Query("SELECT * FROM Entry WHERE _id = :id")
    Entry getById(long id);

    @Query("SELECT COUNT(_id) FROM Entry WHERE date >= :start AND date <= :end")
    long countBetween(DateTime start, DateTime end);

    // TODO: Update when measurements have been migrated to Room
    default long countBetween(DateTime start, DateTime end, Category category) {
        return countBetween(start, end);
    }

    // TODO: Update when measurements have been migrated to Room
    default long countBetween(DateTime start, DateTime end, float minimumValue, float maximumValue) {
        return countBetween(start, end);
    }

    // TODO: Update when measurements have been migrated to Room
    default long countBelow(DateTime start, DateTime end, float maximumValue) {
        return countBetween(start, end);
    }

    // TODO: Update when measurements have been migrated to Room
    default long countAbove(DateTime start, DateTime end, float minimumValue) {
        return countBetween(start, end);
    }

    @Delete
    void delete(Entry entry);
}