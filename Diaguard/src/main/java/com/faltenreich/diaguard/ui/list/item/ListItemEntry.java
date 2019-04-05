package com.faltenreich.diaguard.ui.list.item;

import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;
import com.faltenreich.diaguard.data.entity.FoodEaten;

import java.util.List;

/**
 * Created by Filip on 10.07.2015.
 */
public class ListItemEntry extends ListItemDate {

    private ListItemEntry firstListItemEntryOfDay;
    private Entry entry;
    private List<EntryTag> entryTags;
    private List<FoodEaten> foodEatenList;

    public ListItemEntry(Entry entry, List<EntryTag> entryTags, List<FoodEaten> foodEatenList) {
        super(entry.getDate());
        this.entry = entry;
        this.entryTags = entryTags;
        this.foodEatenList = foodEatenList;
    }

    public ListItemEntry getFirstListItemEntryOfDay() {
        return firstListItemEntryOfDay;
    }

    public void setFirstListItemEntryOfDay(ListItemEntry firstListItemEntryOfDay) {
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
