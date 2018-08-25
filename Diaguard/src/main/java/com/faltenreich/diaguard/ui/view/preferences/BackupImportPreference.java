package com.faltenreich.diaguard.ui.view.preferences;

import android.content.Context;
import android.util.AttributeSet;

import com.faltenreich.diaguard.util.permission.PermissionUseCase;

public class BackupImportPreference extends BackupPreference {

    public BackupImportPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    PermissionUseCase getUseCase() {
        return PermissionUseCase.BACKUP_READ;
    }
}