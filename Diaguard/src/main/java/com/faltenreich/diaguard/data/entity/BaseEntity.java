package com.faltenreich.diaguard.data.entity;

import com.j256.ormlite.field.DatabaseField;

import org.joda.time.DateTime;

import java.util.Objects;

/**
 * Created by Filip on 13.08.2014.
 */
public abstract class BaseEntity {

    public class Column {
        public static final String ID = "_id";
        public static final String CREATED_AT = "createdAt";
        public static final String UPDATED_AT = "updatedAt";
    }

    @DatabaseField(columnName = Column.ID, generatedId = true)
    private long id;

    @DatabaseField(columnName = Column.CREATED_AT)
    private DateTime createdAt;

    @DatabaseField(columnName = Column.UPDATED_AT)
    private DateTime updatedAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(DateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object object) {
        return this.getClass().equals(object.getClass()) && this.getId() == ((BaseEntity) object).getId();
    }
}