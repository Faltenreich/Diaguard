package com.faltenreich.diaguard.adapters.recycler;

import com.faltenreich.diaguard.database.Entry;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Filip on 10.07.2015.
 */
public class RecyclerEntry extends RecyclerItem {

    private DateTime day;
    private List<Entry> entries;

    public RecyclerEntry(DateTime day, List<Entry> entries) {
        this.day = day;
        this.entries = entries;
    }

    public RecyclerEntry(DateTime day) {
        this.day = day;
    }

    public boolean hasEntries() {
        return this.entries != null && this.entries.size() > 0;
    }

    public DateTime getDay() {
        return day;
    }

    public List<Entry> getEntries() {
        return entries;
    }
}
