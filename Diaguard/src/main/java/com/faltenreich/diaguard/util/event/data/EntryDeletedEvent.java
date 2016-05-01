package com.faltenreich.diaguard.util.event.data;

import com.faltenreich.diaguard.data.entity.Entry;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public class EntryDeletedEvent extends BaseEntryEvent {

    public EntryDeletedEvent(Entry entry) {
        super(entry);
    }
}
