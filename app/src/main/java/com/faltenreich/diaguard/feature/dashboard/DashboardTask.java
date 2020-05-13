package com.faltenreich.diaguard.feature.dashboard;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.preference.PreferenceHelper;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.data.database.dao.SqlFunction;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.async.BaseAsyncTask;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.List;

/**
 * Created by Faltenreich on 05.11.2017
 */

public class DashboardTask extends BaseAsyncTask<Void, Void, String[]> {

    public DashboardTask(Context context, OnAsyncProgressListener<String[]> onAsyncProgressListener) {
        super(context, onAsyncProgressListener);
    }

    protected String[] doInBackground(Void... params) {
        int countHypers = 0;
        int countHypos = 0;

        List<Entry> entriesWithBloodSugar = EntryDao.getInstance().getAllWithMeasurementFromToday(BloodSugar.class);
        if (entriesWithBloodSugar != null) {
            for (Entry entry : entriesWithBloodSugar) {
                BloodSugar bloodSugar = (BloodSugar) MeasurementDao.getInstance(BloodSugar.class).getMeasurement(entry);
                if (bloodSugar != null) {
                    float mgDl = bloodSugar.getMgDl();
                    if (mgDl > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                        countHypers++;
                    } else if (mgDl < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                        countHypos++;
                    }
                }
            }
        }
        DateTime now = DateTime.now();
        Interval intervalDay = new Interval(now, now);
        Interval intervalWeek = new Interval(new DateTime(now.minusWeeks(1)), now);
        Interval intervalMonth = new Interval(new DateTime(now.minusMonths(1)), now);
        Interval intervalQuarter = new Interval(new DateTime(now.minusMonths(3)), now);

        float avgDay = MeasurementDao.getInstance(BloodSugar.class).function(SqlFunction.AVG, BloodSugar.Column.MGDL, intervalDay);
        float avgDayCustom = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Category.BLOODSUGAR, avgDay);

        float avgWeek = MeasurementDao.getInstance(BloodSugar.class).function(SqlFunction.AVG, BloodSugar.Column.MGDL, intervalWeek);
        float avgWeekCustom = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Category.BLOODSUGAR, avgWeek);

        float avgMonth = MeasurementDao.getInstance(BloodSugar.class).function(SqlFunction.AVG, BloodSugar.Column.MGDL, intervalMonth);
        float avgMonthCustom = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Category.BLOODSUGAR, avgMonth);

        float avgQuarter = MeasurementDao.getInstance(BloodSugar.class).function(SqlFunction.AVG, BloodSugar.Column.MGDL, intervalQuarter);
        float hbA1cCustom = 0;
        if (avgQuarter > 0) {
            float hbA1c = Helper.calculateHbA1c(avgQuarter);
            hbA1cCustom = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Category.HBA1C, hbA1c);
        }

        return new String[] {
                Integer.toString(entriesWithBloodSugar != null ? entriesWithBloodSugar.size() : 0),
                Integer.toString(countHypers),
                Integer.toString(countHypos),
                avgDayCustom > 0 ? FloatUtils.parseFloat(avgDayCustom) : getContext().getString(R.string.placeholder),
                avgWeekCustom > 0 ? FloatUtils.parseFloat(avgWeekCustom) : getContext().getString(R.string.placeholder),
                avgMonthCustom > 0 ? FloatUtils.parseFloat(avgMonthCustom) : getContext().getString(R.string.placeholder),
                hbA1cCustom > 0 ? String.format("%s %s", FloatUtils.parseFloat(hbA1cCustom), PreferenceHelper.getInstance().getUnitAcronym(Category.HBA1C)) : getContext().getString(R.string.placeholder)
        };
    }
}
