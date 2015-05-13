package com.faltenreich.diaguard.database;

import com.faltenreich.diaguard.helpers.Helper;

import org.joda.time.DateTime;

/**
 * Created by Filip on 13.08.2014.
 */
public abstract class Model {

    private long id;
    private DateTime createdAt;
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

    public void setCreatedAt(String createdAt) {
        this.createdAt = new DateTime(Helper.getDateDatabaseFormat().parseDateTime(createdAt));
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(DateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = new DateTime(Helper.getDateDatabaseFormat().parseDateTime(updatedAt));
    }

    public abstract String getTableName();
}