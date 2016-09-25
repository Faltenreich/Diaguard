package com.faltenreich.diaguard.event;

/**
 * Created by Faltenreich on 26.06.2016.
 */
public class PermissionGrantedEvent extends PermissionEvent {

    public PermissionGrantedEvent(String permission) {
        super(permission);
    }
}
