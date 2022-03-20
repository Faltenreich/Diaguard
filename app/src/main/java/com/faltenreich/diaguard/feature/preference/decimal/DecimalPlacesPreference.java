package com.faltenreich.diaguard.feature.preference.decimal;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.DialogPreference;

import com.faltenreich.diaguard.R;

public class DecimalPlacesPreference extends DialogPreference {

    public DecimalPlacesPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getDialogLayoutResource() {
        return R.layout.preference_decimal_places;
    }
}
