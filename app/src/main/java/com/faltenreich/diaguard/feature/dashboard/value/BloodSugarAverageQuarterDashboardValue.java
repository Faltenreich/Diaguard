package com.faltenreich.diaguard.feature.dashboard.value;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;
import com.faltenreich.diaguard.shared.data.database.dao.SqlFunction;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;

import org.joda.time.DateTime;
import org.joda.time.Interval;

class BloodSugarAverageQuarterDashboardValue implements DashboardValue {

    private final String key;
    private final String value;

    BloodSugarAverageQuarterDashboardValue(Context context) {
        DateTime now = DateTime.now();
        Interval interval = new Interval(new DateTime(now.minusMonths(3)), now);
        float avg = MeasurementDao.getInstance(BloodSugar.class).function(SqlFunction.AVG, BloodSugar.Column.MGDL, interval);
        float avgCustom = PreferenceStore.getInstance().formatDefaultToCustomUnit(Category.BLOODSUGAR, avg);
        key = context.getString(R.string.quarter);
        value = avgCustom > 0 ? FloatUtils.parseFloat(avgCustom) : context.getString(R.string.placeholder);
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
