package com.faltenreich.diaguard.shared.data.database.dao.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.faltenreich.diaguard.shared.data.database.dao.EntryTagDao;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;

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

    default List<EntryTag> getByEntry(Entry entry) {
        return getByEntry(entry.getId());
    }

    @Delete
    int delete(List<EntryTag> entryTags);

    @Query("DELETE FROM EntryTag WHERE entryId = :entryId")
    int deleteByEntry(long entryId);

    default int deleteByEntry(Entry entry) {
        return deleteByEntry(entry.getId());
    }

    @Query("SELECT * FROM EntryTag WHERE tagId = :tagId")
    List<EntryTag> getByTag(long tagId);

    default List<EntryTag> getByTag(Tag tag) {
        return getByTag(tag.getId());
    }

    @Query("SELECT COUNT(tagId) FROM EntryTag")
    long count();

    @Query("SELECT COUNT(tagId) FROM EntryTag WHERE tagId = :tagId")
    long count(long tagId);

    default long countByTag(Tag tag) {
        return count(tag.getId());
    }
}
