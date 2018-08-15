package com.faltenreich.diaguard.data.entity;

import com.j256.ormlite.field.DatabaseField;

public class Tag extends BaseEntity {

    public class Column extends BaseEntity.Column {
        public static final String KEY = "key";
        public static final String NAME = "name";
    }

    @DatabaseField(columnName = Column.KEY)
    private String key;

    @DatabaseField(columnName = Column.NAME)
    private String name;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
