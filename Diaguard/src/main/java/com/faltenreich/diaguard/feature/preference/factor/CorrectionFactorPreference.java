package com.faltenreich.diaguard.feature.preference.factor;

import android.content.Context;
import android.util.AttributeSet;

import com.faltenreich.diaguard.shared.data.preference.PreferenceHelper;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.preference.CorrectionFactorChangedEvent;

/**
 * Created by Filip on 04.11.13.
 */
public class CorrectionFactorPreference extends FactorPreference {

    public CorrectionFactorPreference(Context context, AttributeSet attrs) {
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
        value = PreferenceHelper.getInstance().formatCustomToDefaultUnit(Category.BLOODSUGAR, value);
        PreferenceHelper.getInstance().setCorrectionForHour(hourOfDay, value);
    }

    @Override
    protected float getValueForHour(int hourOfDay) {
        float value = PreferenceHelper.getInstance().getCorrectionForHour(hourOfDay);
        return PreferenceHelper.getInstance().formatDefaultToCustomUnit(Category.BLOODSUGAR, value);
    }

    @Override
    protected void onPreferenceUpdate() {
        super.onPreferenceUpdate();
        Events.post(new CorrectionFactorChangedEvent());
    }
}