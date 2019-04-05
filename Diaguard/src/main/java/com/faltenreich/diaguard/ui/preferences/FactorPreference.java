package com.faltenreich.diaguard.ui.preferences;

import android.content.Context;
import android.util.AttributeSet;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.TimeInterval;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.preference.MealFactorChangedEvent;

/**
 * Created by Filip on 04.11.13.
 */
public class FactorPreference extends ValueListPreference {

    public FactorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void setTimeInterval(TimeInterval interval) {
        PreferenceHelper.getInstance().setFactorInterval(interval);
    }

    @Override
    protected TimeInterval getTimeInterval() {
        return PreferenceHelper.getInstance().getFactorInterval();
    }

    @Override
    protected void storeValueForHour(float value, int hourOfDay) {
        PreferenceHelper.getInstance().setFactorForHour(hourOfDay, value);
    }

    @Override
    protected float getValueForHour(int hourOfDay) {
        return PreferenceHelper.getInstance().getFactorForHour(hourOfDay);
    }

    @Override
    protected void onPreferenceUpdate() {
        super.onPreferenceUpdate();
        Events.post(new MealFactorChangedEvent());
    }
}