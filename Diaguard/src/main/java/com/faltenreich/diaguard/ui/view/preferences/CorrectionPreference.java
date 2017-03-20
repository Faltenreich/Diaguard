package com.faltenreich.diaguard.ui.view.preferences;

import android.content.Context;
import android.util.AttributeSet;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.TimeInterval;

/**
 * Created by Filip on 04.11.13.
 */
public class CorrectionPreference extends ValueListPreference {

    public CorrectionPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void setTimeInterval(TimeInterval interval) {
        PreferenceHelper.getInstance().setCorrectionInterval(interval);
    }

    @Override
    protected TimeInterval getTimeInterval() {
        return PreferenceHelper.getInstance().getCorrectionInterval();
    }

    @Override
    protected void storeValueForHour(float value, int hourOfDay) {
        PreferenceHelper.getInstance().setCorrectionForHour(hourOfDay, value);
    }

    @Override
    protected float getValueForHour(int hourOfDay) {
        return PreferenceHelper.getInstance().getCorrectionForHour(hourOfDay);
    }
}