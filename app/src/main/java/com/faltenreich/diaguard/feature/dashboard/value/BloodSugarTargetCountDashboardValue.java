package com.faltenreich.diaguard.feature.dashboard.value;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;

import java.util.List;

class BloodSugarTargetCountDashboardValue implements DashboardValue {

    private final String key;
    private final String value;

    BloodSugarTargetCountDashboardValue(
        Context context,
        List<BloodSugar> total,
        List<BloodSugar> target
    ) {
        key = context.getString(R.string.hyper);
        value = total.size() > 0
            ? context.getString(
                R.string.dashboard_percentage,
            100 * (float) target.size() / (float) total.size())
            : context.getString(R.string.placeholder);
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
