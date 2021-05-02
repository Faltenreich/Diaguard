package com.faltenreich.diaguard.shared.event.data;

import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;

import java.util.List;

/**
 * Created by Faltenreich on 23.03.2016.
 */
class BaseEntryEvent extends BaseDataEvent<Entry> {

    public final List<EntryTag> entryTags;
    public final List<FoodEaten> foodEatenList;

    BaseEntryEvent(Entry entry, List<EntryTag> entryTags, List<FoodEaten> foodEatenList) {
        super(entry);
        this.entryTags = entryTags;
        this.foodEatenList = foodEatenList;
    }
}
