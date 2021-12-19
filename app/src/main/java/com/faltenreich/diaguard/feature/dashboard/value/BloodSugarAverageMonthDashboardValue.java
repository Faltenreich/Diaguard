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

class BloodSugarAverageMonthDashboardValue implements DashboardValue {

    private final String key;
    private final String value;

    BloodSugarAverageMonthDashboardValue(Context context) {
        DateTime now = DateTime.now();
        Interval intervalMonth = new Interval(new DateTime(now.minusMonths(1)), now);
        float avgMonth = MeasurementDao.getInstance(BloodSugar.class).function(SqlFunction.AVG, BloodSugar.Column.MGDL, intervalMonth);
        float avgMonthCustom = PreferenceStore.getInstance().formatDefaultToCustomUnit(Category.BLOODSUGAR, avgMonth);
        key = context.getString(R.string.month);
        value = avgMonthCustom > 0 ? FloatUtils.parseFloat(avgMonthCustom) : context.getString(R.string.placeholder);
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
