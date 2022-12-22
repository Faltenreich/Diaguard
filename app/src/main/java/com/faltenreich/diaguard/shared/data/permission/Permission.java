package com.faltenreich.diaguard.shared.data.permission;

import android.Manifest;
import android.os.Build;

import androidx.annotation.Nullable;

public enum Permission {

    WRITE_EXTERNAL_STORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE),
    POST_NOTIFICATIONS(Manifest.permission.POST_NOTIFICATIONS);

    public final String code;

    Permission(String code) {
        this.code = code;
    }

    // TODO: Should be more generic
    public boolean isGrantedImplicitly() {
        return (this == WRITE_EXTERNAL_STORAGE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            || (this == POST_NOTIFICATIONS && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU);
    }

    @Nullable
    public static Permission fromCode(String code) {
        for (Permission permission : values()) {
            if (permission.code.equals(code)) {
                return permission;
            }
        }
        return null;
    }
}
