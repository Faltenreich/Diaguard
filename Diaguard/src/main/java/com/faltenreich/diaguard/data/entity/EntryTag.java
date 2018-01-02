package com.faltenreich.diaguard.data.entity;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Faltenreich on 11.09.2016.
 */
public class EntryTag extends BaseEntity {

    public class Column extends BaseEntity.Column {
        public static final String ENTRY = "entry";
        public static final String TAG = "tag";
    }

    @DatabaseField(columnName = Column.ENTRY, foreign = true, foreignAutoRefresh = true)
    private Entry entry;

    @DatabaseField(columnName = Column.TAG, foreign = true, foreignAutoRefresh = true)
    private Tag tag;

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
