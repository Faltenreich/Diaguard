package com.faltenreich.diaguard.ui.view.preferences;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.PermissionRequestEvent;
import com.faltenreich.diaguard.util.permission.Permission;
import com.faltenreich.diaguard.util.permission.PermissionUseCase;

public class ImportPreference extends Preference implements Preference.OnPreferenceClickListener {

    public ImportPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Events.post(new PermissionRequestEvent(Permission.WRITE_EXTERNAL_STORAGE, PermissionUseCase.BACKUP_READ));
        return true;
    }
}