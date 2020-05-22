package com.faltenreich.diaguard.feature.preference.factor;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.feature.preference.data.TimeInterval;

public class MealFactor implements Factor {

    @Override
    public int getTitle() {
        return R.string.meal_factor;
    }

    @Override
    public TimeInterval getTimeInterval() {
        return PreferenceStore.getInstance().getMealFactorInterval();
    }

    @Override
    public void setTimeInterval(TimeInterval timeInterval) {
        PreferenceStore.getInstance().setMealFactorInterval(timeInterval);
    }

    @Override
    public float getValueForHour(int hourOfDay) {
        return PreferenceStore.getInstance().getMealFactorForHour(hourOfDay);
    }

    @Override
    public void setValueForHour(float value, int hourOfDay) {
        PreferenceStore.getInstance().setMealFactorForHour(hourOfDay, value);
    }
}
