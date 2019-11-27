package com.faltenreich.diaguard.data.event.permission;

import com.faltenreich.diaguard.data.event.BaseContextEvent;
import com.faltenreich.diaguard.util.permission.Permission;
import com.faltenreich.diaguard.util.permission.PermissionUseCase;

public class PermissionRequestEvent extends BaseContextEvent<Permission> {

    public PermissionUseCase useCase;

    public PermissionRequestEvent(Permission permission, PermissionUseCase useCase) {
        super(permission);
        this.useCase = useCase;
    }
}
