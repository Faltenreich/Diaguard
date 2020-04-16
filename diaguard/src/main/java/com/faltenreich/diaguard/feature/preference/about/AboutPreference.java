package com.faltenreich.diaguard.feature.preference.about;

import android.content.Context;
import androidx.preference.DialogPreference;
import android.util.AttributeSet;

import com.faltenreich.diaguard.R;

public class AboutPreference extends DialogPreference {

    public AboutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.preference_about);
        setNegativeButtonText(null);
    }
}