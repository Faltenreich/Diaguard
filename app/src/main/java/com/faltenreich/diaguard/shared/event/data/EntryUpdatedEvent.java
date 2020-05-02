package com.faltenreich.diaguard.shared.event.data;

import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;

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
