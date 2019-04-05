package com.faltenreich.diaguard.data.event.data;

import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;
import com.faltenreich.diaguard.data.entity.FoodEaten;

import java.util.List;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public class EntryAddedEvent extends BaseEntryEvent {

    public EntryAddedEvent(Entry entry, List<EntryTag> entryTags, List<FoodEaten> foodEatenList) {
        super(entry, entryTags, foodEatenList);
    }
}
