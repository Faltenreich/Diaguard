package com.faltenreich.diaguard.feature.preference.backup;

import android.content.Context;
import android.util.AttributeSet;

import com.faltenreich.diaguard.shared.data.permission.PermissionUseCase;

public class BackupImportPreference extends BackupPreference {

    public BackupImportPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    PermissionUseCase getUseCase() {
        return PermissionUseCase.BACKUP_READ;
    }
}