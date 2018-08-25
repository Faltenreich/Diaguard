package com.faltenreich.diaguard.ui.view.preferences;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.PermissionRequestEvent;
import com.faltenreich.diaguard.util.permission.Permission;
import com.faltenreich.diaguard.util.permission.PermissionUseCase;

public class BackupPreference extends Preference implements Preference.OnPreferenceClickListener {

    public BackupPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Events.post(new PermissionRequestEvent(Permission.WRITE_EXTERNAL_STORAGE, PermissionUseCase.BACKUP_WRITE));
        return true;
    }
}