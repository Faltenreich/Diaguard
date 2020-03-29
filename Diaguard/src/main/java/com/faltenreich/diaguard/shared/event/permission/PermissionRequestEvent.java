package com.faltenreich.diaguard.shared.event.permission;

import com.faltenreich.diaguard.shared.event.BaseContextEvent;
import com.faltenreich.diaguard.shared.data.permission.Permission;
import com.faltenreich.diaguard.shared.data.permission.PermissionUseCase;

public class PermissionRequestEvent extends BaseContextEvent<Permission> {

    public PermissionUseCase useCase;

    public PermissionRequestEvent(Permission permission, PermissionUseCase useCase) {
        super(permission);
        this.useCase = useCase;
    }
}
