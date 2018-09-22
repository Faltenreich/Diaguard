package com.faltenreich.diaguard.event.data;

import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;
import com.faltenreich.diaguard.data.entity.FoodEaten;

import java.util.List;

/**
 * Created by Faltenreich on 23.03.2016.
 */
class BaseEntryEvent extends BaseDataEvent<Entry> {

    public List<EntryTag> entryTags;
    public List<FoodEaten> foodEatenList;

    BaseEntryEvent(Entry entry, List<EntryTag> entryTags, List<FoodEaten> foodEatenList) {
        super(entry);
        this.entryTags = entryTags;
        this.foodEatenList = foodEatenList;
    }
}
