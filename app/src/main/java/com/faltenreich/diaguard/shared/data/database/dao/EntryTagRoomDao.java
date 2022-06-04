package com.faltenreich.diaguard.shared.data.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;

import java.util.List;

@Dao
public interface EntryTagRoomDao extends EntryTagDao {

    @Query("SELECT * FROM EntryTag")
    List<EntryTag> getAll();

    @Query("SELECT * FROM EntryTag WHERE entryId = :entryId")
    List<EntryTag> getByEntry(long entryId);

    @Override
    default List<EntryTag> getByEntry(Entry entry) {
        return getByEntry(entry.getId());
    }

    @Query("DELETE FROM EntryTag WHERE entryId = :entryId")
    int deleteByEntry(long entryId);

    @Override
    default int deleteByEntry(Entry entry) {
        return deleteByEntry(entry.getId());
    }

    @Query("SELECT * FROM EntryTag WHERE tagId = :tagId")
    List<EntryTag> getByTag(long tagId);

    @Override
    default List<EntryTag> getByTag(Tag tag) {
        return getByTag(tag.getId());
    }

    @Query("SELECT COUNT(tagId) FROM EntryTag WHERE tagId = :tagId")
    long count(long tagId);

    @Override
    default long count(Tag tag) {
        return count(tag.getId());
    }
}
