package com.faltenreich.diaguard.shared.data.permission;

import android.Manifest;

import androidx.annotation.Nullable;

public enum Permission {
    WRITE_EXTERNAL_STORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    public final String code;

    Permission(String code) {
        this.code = code;
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
