package com.faltenreich.diaguard.adapter;

import com.faltenreich.diaguard.data.entity.Entry;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 10.07.2015.
 */
public class LogListEntry extends LogListItem {

    private List<Entry> entries;

    public LogListEntry(DateTime day, List<Entry> entries) {
        super(day);
        this.entries = entries;
    }

    public LogListEntry(DateTime day) {
        super(day);
        this.entries = new ArrayList<>();
    }

    public boolean hasEntries() {
        return this.entries != null && this.entries.size() > 0;
    }

    public List<Entry> getEntries() {
        return entries;
    }
}
