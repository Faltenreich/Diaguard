package com.faltenreich.diaguard.feature.dashboard.value;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;

import java.util.List;

class BloodSugarHyperCountDashboardValue implements DashboardValue {

    private final String key;
    private final String value;

    BloodSugarHyperCountDashboardValue(Context context, List<BloodSugar> bloodSugars) {
        key = context.getString(R.string.hyper);
        value = context.getString(
            R.string.dashboard_percentage,
            100 * (float) countHypers(bloodSugars) / (float) bloodSugars.size()
        );
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    private int countHypers(List<BloodSugar> bloodSugars) {
        float limit = PreferenceStore.getInstance().getLimitHyperglycemia();
        int hyperCount = 0;
        for (BloodSugar bloodSugar : bloodSugars) {
            float mgDl = bloodSugar.getMgDl();
            if (mgDl > limit) {
                hyperCount++;
            }
        }
        return hyperCount;
    }
}
