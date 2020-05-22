package com.faltenreich.diaguard.feature.preference.factor;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.feature.preference.data.TimeInterval;
import com.faltenreich.diaguard.shared.data.database.entity.Category;

public class CorrectionFactor implements Factor {

    @Override
    public int getTitle() {
        return R.string.correction_value;
    }

    @Override
    public TimeInterval getTimeInterval() {
        return PreferenceStore.getInstance().getCorrectionInterval();
    }

    @Override
    public void setTimeInterval(TimeInterval timeInterval) {
        PreferenceStore.getInstance().setCorrectionInterval(timeInterval);
    }

    @Override
    public float getValueForHour(int hourOfDay) {
        float value = PreferenceStore.getInstance().getCorrectionForHour(hourOfDay);
        return PreferenceStore.getInstance().formatDefaultToCustomUnit(Category.BLOODSUGAR, value);
    }

    @Override
    public void setValueForHour(float value, int hourOfDay) {
        value = PreferenceStore.getInstance().formatCustomToDefaultUnit(Category.BLOODSUGAR, value);
        PreferenceStore.getInstance().setCorrectionForHour(hourOfDay, value);
    }
}
