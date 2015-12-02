package com.faltenreich.diaguard.adapter;

import com.faltenreich.diaguard.data.entity.Entry;

import org.joda.time.DateTime;

/**
 * Created by Filip on 10.07.2015.
 */
public class ListItemEntry extends ListItem {

    private Entry entry;

    public ListItemEntry(Entry entry) {
        super();
        this.entry = entry;
    }

    public Entry getEntry() {
        return entry;
    }

    @Override
    public DateTime getDateTime() {
        return entry.getDate();
    }
}
