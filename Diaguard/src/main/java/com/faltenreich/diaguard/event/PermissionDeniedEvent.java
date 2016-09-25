package com.faltenreich.diaguard.event;

/**
 * Created by Faltenreich on 26.06.2016.
 */
public class PermissionDeniedEvent extends PermissionEvent {

    public PermissionDeniedEvent(String permission) {
        super(permission);
    }
}
