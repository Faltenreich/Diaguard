package com.faltenreich.diaguard.shared.event.data;

import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;

import java.util.List;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public class EntryDeletedEvent extends BaseEntryEvent {

    public EntryDeletedEvent(Entry entry, List<EntryTag> entryTags, List<FoodEaten> foodEatenList) {
        super(entry, entryTags, foodEatenList);
    }
}
