package com.faltenreich.diaguard.event;

abstract class PermissionEvent extends BaseContextEvent<String> {

    PermissionEvent(String permission) {
        super(permission);
    }
}
