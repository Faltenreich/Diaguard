package com.faltenreich.diaguard.data.entity;

import com.j256.ormlite.field.DatabaseField;

import org.joda.time.DateTime;

/**
 * Created by Filip on 13.08.2014.
 */
public abstract class Model {

    public static final String ID = "id";
    public static final String CREATED_AT = "createdat";
    public static final String UPDATED_AT = "updatedat";

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private DateTime createdAt;

    @DatabaseField
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
}