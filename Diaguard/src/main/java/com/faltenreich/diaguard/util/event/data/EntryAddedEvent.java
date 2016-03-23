package com.faltenreich.diaguard.util.event.data;

import com.faltenreich.diaguard.data.entity.Entry;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public class EntryAddedEvent extends BaseEntryEvent {

    public EntryAddedEvent(Entry entry) {
        super(entry);
    }
}
