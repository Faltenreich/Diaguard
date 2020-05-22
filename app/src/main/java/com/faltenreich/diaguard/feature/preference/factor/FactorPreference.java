package com.faltenreich.diaguard.feature.preference.factor;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.DialogPreference;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.TimeInterval;

public abstract class FactorPreference extends DialogPreference {

    FactorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getDialogLayoutResource() {
        return R.layout.fragment_factor;
    }

    protected void onPreferenceUpdate() {

    }

    abstract void setTimeInterval(TimeInterval interval);

    abstract TimeInterval getTimeInterval();

    abstract void storeValueForHour(float value, int hourOfDay);

    abstract float getValueForHour(int hourOfDay);
}