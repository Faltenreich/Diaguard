package com.faltenreich.diaguard.event.data;

import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public class EntryUpdatedEvent extends BaseEntryEvent {

    public DateTime originalDate;

    public EntryUpdatedEvent(Entry entry, List<EntryTag> entryTags, DateTime originalDate) {
        super(entry, entryTags);
        this.originalDate = originalDate;
    }
}
