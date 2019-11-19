package com.faltenreich.diaguard.util.permission;

import androidx.annotation.Nullable;

public enum PermissionUseCase {
    EXPORT,
    EXPORT_HISTORY,
    BACKUP_WRITE,
    BACKUP_READ;

    public int requestCode = ordinal() + 123;

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