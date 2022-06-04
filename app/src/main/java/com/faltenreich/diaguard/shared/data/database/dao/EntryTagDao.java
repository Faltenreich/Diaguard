package com.faltenreich.diaguard.shared.data.database.dao;

import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;

import java.util.List;

public interface EntryTagDao {

    long createOrUpdate(EntryTag entryTag);

    void createOrUpdate(List<EntryTag> entryTags);

    List<EntryTag> getByEntry(long entryId);

    List<EntryTag> getByTag(long tagId);

    int delete(List<EntryTag> entryTags);

    int deleteByEntry(long entryId);

    long count();

    long countByTag(long tagId);
}
