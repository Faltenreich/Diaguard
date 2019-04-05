package com.faltenreich.diaguard.ui.preferences;

import android.content.Context;
import android.util.AttributeSet;

import com.faltenreich.diaguard.util.permission.PermissionUseCase;

public class BackupExportPreference extends BackupPreference {

    public BackupExportPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    PermissionUseCase getUseCase() {
        return PermissionUseCase.BACKUP_WRITE;
    }
}