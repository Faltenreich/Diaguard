package com.faltenreich.diaguard.shared.data.permission;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.annotation.Nullable;

@SuppressLint("InlinedApi")
public enum Permission {

    WRITE_EXTERNAL_STORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE),
    POST_NOTIFICATIONS(Manifest.permission.POST_NOTIFICATIONS),
    ;

    private final String code;

    Permission(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Nullable
    public static Permission fromCode(String code) {
        for (Permission permission : values()) {
            if (permission.getCode().equals(code)) {
                return permission;
            }
        }
        return null;
    }
}
