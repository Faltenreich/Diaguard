package com.faltenreich.diaguard.shared.data.database.entity;

import com.j256.ormlite.field.DatabaseField;

import org.joda.time.DateTime;

public class BaseServerEntity extends BaseEntity {

    public class Column extends BaseEntity.Column {
        public static final String SERVER_ID = "serverId";
        public static final String DELETED_AT = "deletedAt";
    }

    @DatabaseField(columnName = Column.SERVER_ID)
    private String serverId;

    @DatabaseField(columnName = Column.DELETED_AT)
    private DateTime deletedAt;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public DateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(DateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}
