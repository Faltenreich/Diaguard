package com.faltenreich.diaguard.data.entity;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Faltenreich on 01.01.2018
 */

public class Tag extends BaseEntity {

    public class Column extends BaseEntity.Column {
        public static final String NAME = "name";
    }

    @DatabaseField(columnName = Column.NAME)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
