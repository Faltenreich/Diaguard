package com.faltenreich.diaguard.event.data;

import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;

import java.util.List;

/**
 * Created by Faltenreich on 23.03.2016.
 */
class BaseEntryEvent extends BaseDataEvent<Entry> {

    public List<EntryTag> entryTags;

    BaseEntryEvent(Entry entry, List<EntryTag> entryTags) {
        super(entry);
        this.entryTags = entryTags;
    }
}
