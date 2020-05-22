package com.faltenreich.diaguard.feature.preference.factor;

import com.faltenreich.diaguard.feature.preference.data.TimeInterval;

interface Factor {

    TimeInterval getTimeInterval();
    void setTimeInterval(TimeInterval timeInterval);

    float getValueForHour(int hourOfDay);
    void setValueForHour(float value, int hourOfDay);
}
