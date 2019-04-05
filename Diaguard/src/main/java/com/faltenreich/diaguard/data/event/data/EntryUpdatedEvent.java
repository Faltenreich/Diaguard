package com.faltenreich.diaguard.data.event.data;

import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;
import com.faltenreich.diaguard.data.entity.FoodEaten;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public class EntryUpdatedEvent extends BaseEntryEvent {

    public DateTime originalDate;

    public EntryUpdatedEvent(Entry entry, List<EntryTag> entryTags, DateTime originalDate, List<FoodEaten> foodEatenList) {
        super(entry, entryTags, foodEatenList);
        this.originalDate = originalDate;
    }
}
