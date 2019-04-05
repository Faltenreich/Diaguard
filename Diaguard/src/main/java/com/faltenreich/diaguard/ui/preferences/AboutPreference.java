package com.faltenreich.diaguard.ui.preferences;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

import com.faltenreich.diaguard.R;

public class AboutPreference extends DialogPreference {

    public AboutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.preference_about);
        setNegativeButtonText(null);
    }
}