package com.faltenreich.diaguard.adapter.list;

import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;

import java.util.List;

/**
 * Created by Filip on 10.07.2015.
 */
public class ListItemEntry extends ListItemDate {

    private ListItemEntry firstListItemEntryOfDay;
    private Entry entry;
    private List<EntryTag> entryTags;

    public ListItemEntry(Entry entry, List<EntryTag> entryTags) {
        super(entry.getDate());
        this.entry = entry;
        this.entryTags = entryTags;
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
}
