package com.faltenreich.diaguard.event;

/**
 * Created by Faltenreich on 26.06.2016.
 */
public abstract class PermissionEvent extends BaseContextEvent<String> {

    public PermissionEvent(String permission) {
        super(permission);
    }
}
