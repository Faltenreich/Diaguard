package com.faltenreich.diaguard.data.entity;

import com.faltenreich.diaguard.data.Backupable;
import com.j256.ormlite.field.DatabaseField;

public class Tag extends BaseEntity implements Backupable {

    public static final String BACKUP_KEY = "tag";

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

    @Override
    public String getKeyForBackup() {
        return BACKUP_KEY;
    }

    @Override
    public String[] getValuesForBackup() {
        return new String[]{name};
    }
}
