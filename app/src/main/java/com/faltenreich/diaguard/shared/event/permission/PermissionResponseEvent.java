package com.faltenreich.diaguard.shared.event.permission;

import com.faltenreich.diaguard.shared.event.BaseContextEvent;
import com.faltenreich.diaguard.shared.data.permission.Permission;
import com.faltenreich.diaguard.shared.data.permission.PermissionUseCase;

public class PermissionResponseEvent extends BaseContextEvent<Permission> {

    public final PermissionUseCase useCase;
    public final boolean isGranted;

    public PermissionResponseEvent(Permission permission, PermissionUseCase useCase, boolean isGranted) {
        super(permission);
        this.useCase = useCase;
        this.isGranted = isGranted;
    }
}
