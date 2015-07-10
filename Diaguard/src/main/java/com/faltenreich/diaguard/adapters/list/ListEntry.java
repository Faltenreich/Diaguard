package com.faltenreich.diaguard.adapters.list;

import com.faltenreich.diaguard.database.Entry;

/**
 * Created by Filip on 20.08.2014.
 */
public class ListEntry implements ListItem {

    private final Entry entry;

    public ListEntry(Entry entry) {
        this.entry = entry;
    }

    public Entry getEntry() {
        return this.entry;
    }

    @Override
    public boolean isSection() {
        return false;
    }
}