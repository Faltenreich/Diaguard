package com.faltenreich.diaguard.ui.view.preferences;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.faltenreich.diaguard.ui.activity.BaseActivity;
import com.faltenreich.diaguard.util.FileUtils;
import com.faltenreich.diaguard.util.export.Export;

public class ImportPreference extends Preference implements Preference.OnPreferenceClickListener {

    public ImportPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (getContext() instanceof BaseActivity) {
            FileUtils.searchFiles((BaseActivity) getContext(), Export.CSV_MIME_TYPE, BaseActivity.REQUEST_CODE_BACKUP_IMPORT);
        }
        return true;
    }
}