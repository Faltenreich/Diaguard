package com.faltenreich.diaguard.feature.preference.factor;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.feature.preference.data.TimeInterval;
import com.faltenreich.diaguard.shared.data.database.entity.Category;

public class BasalRateFactor implements Factor {

    @Override
    public int getTitle() {
        return R.string.basal_rate;
    }

    @Override
    public TimeInterval getTimeInterval() {
        return PreferenceStore.getInstance().getBasalRateFactorInterval();
    }

    @Override
    public void setTimeInterval(TimeInterval timeInterval) {
        PreferenceStore.getInstance().setBasalRateFactorInterval(timeInterval);
    }

    @Override
    public float getValueForHour(int hourOfDay) {
        float value = PreferenceStore.getInstance().getBasalRateFactorForHour(hourOfDay);
        return PreferenceStore.getInstance().formatDefaultToCustomUnit(Category.INSULIN, value);
    }

    @Override
    public void setValueForHour(float value, int hourOfDay) {
        value = PreferenceStore.getInstance().formatCustomToDefaultUnit(Category.INSULIN, value);
        PreferenceStore.getInstance().setBasalRateFactorForHour(hourOfDay, value);
    }
}
