package com.faltenreich.diaguard.feature.log.entry;

import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.feature.log.LogListItem;

import java.util.List;

/**
 * Created by Filip on 10.07.2015.
 */
public class LogEntryListItem extends LogListItem {

    private LogEntryListItem firstListItemEntryOfDay;
    private Entry entry;
    private List<EntryTag> entryTags;
    private List<FoodEaten> foodEatenList;

    public LogEntryListItem(Entry entry, List<EntryTag> entryTags, List<FoodEaten> foodEatenList) {
        super(entry.getDate());
        this.entry = entry;
        this.entryTags = entryTags;
        this.foodEatenList = foodEatenList;
    }

    public LogEntryListItem getFirstListItemEntryOfDay() {
        return firstListItemEntryOfDay;
    }

    public void setFirstListItemEntryOfDay(LogEntryListItem firstListItemEntryOfDay) {
        this.firstListItemEntryOfDay = firstListItemEntryOfDay;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public List<EntryTag> getEntryTags() {
        return entryTags;
    }

    public void setEntryTags(List<EntryTag> entryTags) {
        this.entryTags = entryTags;
    }

    public List<FoodEaten> getFoodEatenList() {
        return foodEatenList;
    }

    public void setFoodEatenList(List<FoodEaten> foodEatenList) {
        this.foodEatenList = foodEatenList;
    }
}
