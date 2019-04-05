package com.faltenreich.diaguard.ui.preferences;

import android.content.Context;
import android.util.AttributeSet;

import com.faltenreich.diaguard.event.PermissionRequestEvent;
import com.faltenreich.diaguard.util.permission.Permission;
import com.faltenreich.diaguard.util.permission.PermissionUseCase;

abstract class BackupPreference extends ClickablePreference<PermissionRequestEvent> {

    public BackupPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    abstract PermissionUseCase getUseCase();

    @Override
    PermissionRequestEvent getEvent() {
        return new PermissionRequestEvent(Permission.WRITE_EXTERNAL_STORAGE, getUseCase());
    }
}