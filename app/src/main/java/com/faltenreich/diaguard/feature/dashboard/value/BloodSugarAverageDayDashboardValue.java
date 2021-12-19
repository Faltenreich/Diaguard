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

class BloodSugarAverageDayDashboardValue implements DashboardValue {

    private final String key;
    private final String value;

    BloodSugarAverageDayDashboardValue(Context context) {
        DateTime now = DateTime.now();
        Interval intervalDay = new Interval(now, now);
        float avgDay = MeasurementDao.getInstance(BloodSugar.class).function(SqlFunction.AVG, BloodSugar.Column.MGDL, intervalDay);
        float avgDayCustom = PreferenceStore.getInstance().formatDefaultToCustomUnit(Category.BLOODSUGAR, avgDay);
        key = context.getString(R.string.day);
        value = avgDayCustom > 0 ? FloatUtils.parseFloat(avgDayCustom) : context.getString(R.string.placeholder);
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
