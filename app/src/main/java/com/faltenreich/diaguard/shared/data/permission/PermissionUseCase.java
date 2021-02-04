package com.faltenreich.diaguard.shared.data.permission;

import androidx.annotation.Nullable;

public enum PermissionUseCase {
    EXPORT,
    EXPORT_HISTORY,
    EXPORT_DELETE,
    BACKUP_WRITE,
    BACKUP_READ;

    public final int requestCode = ordinal() + 123;

    @Nullable
    public static PermissionUseCase fromRequestCode(int requestCode) {
        for (PermissionUseCase useCase : values()) {
            if (useCase.requestCode == requestCode) {
                return useCase;
            }
        }
        return null;
    }
}