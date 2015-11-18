package com.faltenreich.diaguard.data.entity;

import com.j256.ormlite.field.DatabaseField;

import org.joda.time.DateTime;

/**
 * Created by Filip on 13.08.2014.
 */
public abstract class BaseEntity {

    public class Column {
        public static final String ID = "_id";
    }

    @DatabaseField(columnName = Column.ID, generatedId = true)
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}