package com.faltenreich.diaguard.shared.data.database.dao;

import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;

import java.util.List;

public interface EntryTagDao {

    Long create(EntryTag entryTag);

    void update(EntryTag entryTag);

    List<EntryTag> getByEntry(Entry entry);


    int delete(List<EntryTag> entryTags);

    int deleteByEntry(Entry entry);

    List<EntryTag> getByTag(Tag tag);

    long count();

    long countByTag(Tag tag);
}
