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

class BloodSugarAverageWeekDashboardValue implements DashboardValue {

    private final String key;
    private final String value;

    BloodSugarAverageWeekDashboardValue(Context context) {
        DateTime now = DateTime.now();
        Interval intervalWeek = new Interval(new DateTime(now.minusWeeks(1)), now);
        float avgWeek = MeasurementDao.getInstance(BloodSugar.class).function(SqlFunction.AVG, BloodSugar.Column.MGDL, intervalWeek);
        float avgWeekCustom = PreferenceStore.getInstance().formatDefaultToCustomUnit(Category.BLOODSUGAR, avgWeek);
        key = context.getString(R.string.week);
        value = avgWeekCustom > 0 ? FloatUtils.parseFloat(avgWeekCustom) : context.getString(R.string.placeholder);
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
