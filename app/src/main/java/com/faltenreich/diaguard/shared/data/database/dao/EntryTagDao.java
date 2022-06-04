package com.faltenreich.diaguard.shared.data.database.dao;

import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;

import java.util.List;

public interface EntryTagDao {

    List<EntryTag> getByEntry(Entry entry);

    int deleteByEntry(Entry entry);

    List<EntryTag> getByTag(Tag tag);

    long count(Tag tag);
}
