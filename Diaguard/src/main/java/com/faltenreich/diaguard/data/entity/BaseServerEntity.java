package com.faltenreich.diaguard.data.entity;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Faltenreich on 11.09.2016.
 */
public class BaseServerEntity extends BaseEntity {

    public class Column extends BaseEntity.Column {
        public static final String SERVER_ID = "serverId";
    }

    @DatabaseField(columnName = Food.Column.SERVER_ID)
    private String serverId;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public boolean isSynchronized() {
        return getServerId() != null;
    }
}
