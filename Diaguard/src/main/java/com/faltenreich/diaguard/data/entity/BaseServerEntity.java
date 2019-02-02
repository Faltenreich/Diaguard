package com.faltenreich.diaguard.data.entity;

import com.j256.ormlite.field.DatabaseField;

public class BaseServerEntity extends BaseEntity {

    public class Column extends BaseEntity.Column {
        public static final String SERVER_ID = "serverId";
        public static final String IS_DELETED = "isDeleted";
    }

    @DatabaseField(columnName = Column.SERVER_ID)
    private String serverId;

    @DatabaseField(columnName = Column.IS_DELETED)
    private boolean isDeleted;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
