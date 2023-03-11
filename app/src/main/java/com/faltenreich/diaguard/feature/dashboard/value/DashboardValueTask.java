package com.faltenreich.diaguard.feature.dashboard.value;

import android.content.Context;

import com.faltenreich.diaguard.shared.data.async.BaseAsyncTask;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;

import java.util.ArrayList;
import java.util.List;

public class DashboardValueTask extends BaseAsyncTask<Void, Void, DashboardValue[]> {

    public DashboardValueTask(Context context, OnAsyncProgressListener<DashboardValue[]> onAsyncProgressListener) {
        super(context, onAsyncProgressListener);
    }

    protected DashboardValue[] doInBackground(Void... params) {
        List<BloodSugar> bloodSugars = getBloodSugarOfToday();
        return new DashboardValue[] {
            new BloodSugarCountDashboardValue(getContext(), bloodSugars),
            new BloodSugarTargetCountDashboardValue(getContext(), bloodSugars),
            new BloodSugarHyperCountDashboardValue(getContext(), bloodSugars),
            new BloodSugarHypoCountDashboardValue(getContext(), bloodSugars),
            new BloodSugarAverageDayDashboardValue(getContext()),
            new BloodSugarAverageWeekDashboardValue(getContext()),
            new BloodSugarAverageMonthDashboardValue(getContext()),
            new BloodSugarAverageQuarterDashboardValue(getContext()),
            new HbA1cDashboardValue(getContext())
        };
    }

    private List<BloodSugar> getBloodSugarOfToday() {
        List<BloodSugar> bloodSugars = new ArrayList<>();
        List<Entry> entriesWithBloodSugar = EntryDao.getInstance().getAllWithMeasurementFromToday(BloodSugar.class);
        if (entriesWithBloodSugar != null) {
            for (Entry entry : entriesWithBloodSugar) {
                BloodSugar bloodSugar = (BloodSugar) MeasurementDao.getInstance(BloodSugar.class).getMeasurement(entry);
                if (bloodSugar != null) {
                    bloodSugars.add(bloodSugar);
                }
            }
        }
        return bloodSugars;
    }
}
