package com.faltenreich.diaguard.feature.dashboard.value;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;

import java.util.List;

class BloodSugarTargetCountDashboardValue implements DashboardValue {

    private final String key;
    private final String value;

    BloodSugarTargetCountDashboardValue(Context context, List<BloodSugar> bloodSugars) {
        key = context.getString(R.string.hyper);
        value = context.getString(
            R.string.dashboard_percentage,
            100 * (float) countTargets(bloodSugars) / (float) bloodSugars.size()
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

    private int countTargets(List<BloodSugar> bloodSugars) {
        float limitHypo = PreferenceStore.getInstance().getLimitHypoglycemia();
        float limitHyper = PreferenceStore.getInstance().getLimitHyperglycemia();
        int targetCount = 0;
        for (BloodSugar bloodSugar : bloodSugars) {
            float mgDl = bloodSugar.getMgDl();
            if (mgDl >= limitHypo && mgDl <= limitHyper) {
                targetCount++;
            }
        }
        return targetCount;
    }
}
