package com.faltenreich.diaguard.feature.dashboard.value;

import android.content.Context;

import com.faltenreich.diaguard.R;

class BloodSugarCountDashboardValue implements DashboardValue {

    private final String key;
    private final String value;

    BloodSugarCountDashboardValue(Context context, int count) {
        key = context.getString(R.string.measurements);
        value = Integer.toString(count);
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }
}
