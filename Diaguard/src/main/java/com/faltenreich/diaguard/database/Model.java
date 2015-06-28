package com.faltenreich.diaguard.database;

import com.faltenreich.diaguard.helpers.Helper;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import org.joda.time.DateTime;

/**
 * Created by Filip on 13.08.2014.
 */
public abstract class Model {

    public static final String ID = "id";
    public static final String CREATED_AT = "createdat";
    public static final String UPDATED_AT = "updatedat";

    @DatabaseField(generatedId = true, columnDefinition = "INTEGER PRIMARY KEY AUTOINCREMENT")
    private long id;

    @DatabaseField(dataType = DataType.DATE_TIME)
    private DateTime createdAt;

    @DatabaseField(dataType = DataType.DATE_TIME)
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
}