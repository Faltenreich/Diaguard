package com.faltenreich.diaguard.feature.preference.about;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.DialogPreference;

import com.faltenreich.diaguard.R;

public class AboutPreference extends DialogPreference {

    public AboutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setNegativeButtonText(null);
        setPositiveButtonText(R.string.close);
    }

    @Override
    public int getDialogLayoutResource() {
        return R.layout.preference_about;
    }
}