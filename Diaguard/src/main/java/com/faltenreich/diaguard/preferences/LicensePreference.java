package com.faltenreich.diaguard.preferences;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

import com.faltenreich.diaguard.R;

/**
 * Created by Filip on 04.11.13.
 */
public class LicensePreference extends DialogPreference {

    public LicensePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.preference_license);
        setNegativeButtonText(null);
    }
}