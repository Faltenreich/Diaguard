package com.faltenreich.diaguard.database;

/**
 * Created by Filip on 13.08.2014.
 */
public abstract class Model {

    public long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public abstract String getTableName();
}