package com.faltenreich.diaguard.data.event;

import com.faltenreich.diaguard.util.permission.Permission;
import com.faltenreich.diaguard.util.permission.PermissionUseCase;

public class PermissionResponseEvent extends BaseContextEvent<Permission> {

    public PermissionUseCase useCase;
    public boolean isGranted;

    public PermissionResponseEvent(Permission permission, PermissionUseCase useCase, boolean isGranted) {
        super(permission);
        this.useCase = useCase;
        this.isGranted = isGranted;
    }
}
