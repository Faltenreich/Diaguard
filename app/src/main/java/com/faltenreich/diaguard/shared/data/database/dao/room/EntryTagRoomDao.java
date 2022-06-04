package com.faltenreich.diaguard.shared.data.database.dao.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.faltenreich.diaguard.shared.data.database.dao.EntryTagDao;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;

import java.util.List;

@Dao
public interface EntryTagRoomDao extends EntryTagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createOrUpdate(EntryTag entryTag);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createOrUpdate(List<EntryTag> entryTags);

    @Query("SELECT * FROM EntryTag")
    List<EntryTag> getAll();

    @Query("SELECT * FROM EntryTag WHERE entryId = :entryId")
    List<EntryTag> getByEntry(long entryId);

    @Query("SELECT * FROM EntryTag WHERE tagId = :tagId")
    List<EntryTag> getByTag(long tagId);

    @Delete
    int delete(List<EntryTag> entryTags);

    @Query("DELETE FROM EntryTag WHERE entryId = :entryId")
    int deleteByEntry(long entryId);

    @Query("SELECT COUNT(tagId) FROM EntryTag")
    long count();

    @Query("SELECT COUNT(tagId) FROM EntryTag WHERE tagId = :tagId")
    long countByTag(long tagId);
}
