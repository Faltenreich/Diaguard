package com.faltenreich.diaguard.preferences;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;

import com.faltenreich.diaguard.R;

/**
 * Created by Filip on 04.11.13.
 */
public class PrivacyPreference extends DialogPreference {

    public PrivacyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.preference_privacy);
        setNegativeButtonText(null);
    }

    @Override
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);

        if(Build.VERSION.SDK_INT <= 10) {
            view.setBackgroundColor(Color.WHITE);
        }
    }
}