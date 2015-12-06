package com.faltenreich.diaguard.adapter.list;

import com.faltenreich.diaguard.data.entity.Entry;

import org.joda.time.DateTime;

/**
 * Created by Filip on 10.07.2015.
 */
public class ListItemEntry extends ListItem {

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
}
