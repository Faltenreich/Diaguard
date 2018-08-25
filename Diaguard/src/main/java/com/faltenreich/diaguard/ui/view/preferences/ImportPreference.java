package com.faltenreich.diaguard.ui.view.preferences;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.faltenreich.diaguard.ui.activity.BaseActivity;

public class ImportPreference extends Preference implements Preference.OnPreferenceClickListener {

    public ImportPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (getContext() instanceof BaseActivity) {
            ((BaseActivity) getContext()).importBackup();
        }
        return true;
    }
}