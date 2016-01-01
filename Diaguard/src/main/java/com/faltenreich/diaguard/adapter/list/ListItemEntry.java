package com.faltenreich.diaguard.adapter.list;

import com.faltenreich.diaguard.data.entity.Entry;

/**
 * Created by Filip on 10.07.2015.
 */
public class ListItemEntry extends ListItemDate {

    private ListItemEntry firstListItemEntryOfDay;
    private Entry entry;

    public ListItemEntry(Entry entry) {
        super(entry.getDate());
        this.entry = entry;
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
}
