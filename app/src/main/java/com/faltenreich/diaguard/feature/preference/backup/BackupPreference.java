package com.faltenreich.diaguard.feature.preference.backup;

import android.content.Context;
import android.util.AttributeSet;

import com.faltenreich.diaguard.shared.event.permission.PermissionRequestEvent;
import com.faltenreich.diaguard.shared.data.permission.Permission;
import com.faltenreich.diaguard.shared.data.permission.PermissionUseCase;

abstract class BackupPreference extends ClickablePreference<PermissionRequestEvent> {

    BackupPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    abstract PermissionUseCase getUseCase();

    @Override
    public PermissionRequestEvent getEvent() {
        return new PermissionRequestEvent(Permission.WRITE_EXTERNAL_STORAGE, getUseCase());
    }
}