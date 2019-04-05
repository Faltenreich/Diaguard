package com.faltenreich.diaguard.ui.preferences;

import android.content.Context;
import android.util.AttributeSet;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.TimeInterval;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.preference.CorrectionFactorChangedEvent;

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
        value = PreferenceHelper.getInstance().formatCustomToDefaultUnit(Measurement.Category.BLOODSUGAR, value);
        PreferenceHelper.getInstance().setCorrectionForHour(hourOfDay, value);
    }

    @Override
    protected float getValueForHour(int hourOfDay) {
        float value = PreferenceHelper.getInstance().getCorrectionForHour(hourOfDay);
        return PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR, value);
    }

    @Override
    protected void onPreferenceUpdate() {
        super.onPreferenceUpdate();
        Events.post(new CorrectionFactorChangedEvent());
    }
}