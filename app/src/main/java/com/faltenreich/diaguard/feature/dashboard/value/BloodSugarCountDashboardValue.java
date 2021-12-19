package com.faltenreich.diaguard.feature.dashboard.value;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;

import java.util.List;

class BloodSugarCountDashboardValue implements DashboardValue {

    private final String key;
    private final String value;

    BloodSugarCountDashboardValue(Context context, List<BloodSugar> bloodSugars) {
        key = context.getString(R.string.measurements);
        value = Integer.toString(bloodSugars != null ? bloodSugars.size() : 0);
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
