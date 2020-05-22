package com.faltenreich.diaguard.feature.preference.factor;

import android.content.Context;
import android.util.AttributeSet;

import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.feature.preference.data.TimeInterval;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.preference.MealFactorChangedEvent;

/**
 * Created by Filip on 04.11.13.
 */
public class MealFactorPreference extends FactorPreference {

    public MealFactorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void setTimeInterval(TimeInterval interval) {
        PreferenceStore.getInstance().setFactorInterval(interval);
    }

    @Override
    protected TimeInterval getTimeInterval() {
        return PreferenceStore.getInstance().getFactorInterval();
    }

    @Override
    protected void storeValueForHour(float value, int hourOfDay) {
        PreferenceStore.getInstance().setFactorForHour(hourOfDay, value);
    }

    @Override
    protected float getValueForHour(int hourOfDay) {
        return PreferenceStore.getInstance().getFactorForHour(hourOfDay);
    }

    @Override
    protected void onPreferenceUpdate() {
        super.onPreferenceUpdate();
        Events.post(new MealFactorChangedEvent());
    }
}