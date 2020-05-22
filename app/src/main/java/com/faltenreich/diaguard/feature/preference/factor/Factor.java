package com.faltenreich.diaguard.feature.preference.factor;

import androidx.annotation.StringRes;

import com.faltenreich.diaguard.feature.preference.data.TimeInterval;

interface Factor {

    @StringRes int getTitle();

    TimeInterval getTimeInterval();
    void setTimeInterval(TimeInterval timeInterval);

    float getValueForHour(int hourOfDay);
    void setValueForHour(float value, int hourOfDay);
}
