package com.faltenreich.diaguard.feature.dashboard.value;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;

import java.util.List;

class BloodSugarHypoCountDashboardValue implements DashboardValue {

    private final String key;
    private final String value;

    BloodSugarHypoCountDashboardValue(Context context, List<BloodSugar> bloodSugars) {
        key = context.getString(R.string.hypo);
        value = context.getString(
            R.string.dashboard_percentage,
            100 * (float) countHypos(bloodSugars) / (float) bloodSugars.size()
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

    private int countHypos(List<BloodSugar> bloodSugars) {
        float limit = PreferenceStore.getInstance().getLimitHypoglycemia();
        int hypoCount = 0;
        for (BloodSugar bloodSugar : bloodSugars) {
            float mgDl = bloodSugar.getMgDl();
            if (mgDl < limit) {
                hypoCount++;
            }
        }
        return hypoCount;
    }
}
