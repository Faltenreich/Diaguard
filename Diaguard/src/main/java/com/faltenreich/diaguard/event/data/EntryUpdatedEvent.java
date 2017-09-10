package com.faltenreich.diaguard.event.data;

import com.faltenreich.diaguard.data.entity.Entry;

import org.joda.time.DateTime;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public class EntryUpdatedEvent extends BaseEntryEvent {

    public DateTime originalDate;

    public EntryUpdatedEvent(Entry entry, DateTime originalDate) {
        super(entry);
        this.originalDate = originalDate;
    }
}
