package com.faltenreich.diaguard.event.data;

import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;

import java.util.List;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public class EntryAddedEvent extends BaseEntryEvent {

    public EntryAddedEvent(Entry entry, List<EntryTag> entryTags) {
        super(entry, entryTags);
    }
}
