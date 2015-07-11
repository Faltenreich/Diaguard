package com.faltenreich.diaguard.ui.recycler;

import com.faltenreich.diaguard.database.Entry;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Filip on 10.07.2015.
 */
public class RecyclerEntry extends RecyclerItem {

    private List<Entry> entries;

    public RecyclerEntry(DateTime day, List<Entry> entries) {
        super(day);
        this.entries = entries;
    }

    public boolean hasEntries() {
        return this.entries != null && this.entries.size() > 0;
    }

    public List<Entry> getEntries() {
        return entries;
    }
}
