package com.faltenreich.diaguard.adapter;

import com.faltenreich.diaguard.data.entity.Entry;

import org.joda.time.DateTime;

/**
 * Created by Filip on 10.07.2015.
 */
public class ListItemEntry extends ListItem {

    private Entry entry;

    public ListItemEntry(Entry entry, int sectionManager, int sectionFirstPosition) {
        super(false, sectionManager, sectionFirstPosition);
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
