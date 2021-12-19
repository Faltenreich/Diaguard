package com.faltenreich.diaguard.feature.dashboard.value;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;
import com.faltenreich.diaguard.shared.data.database.dao.SqlFunction;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.HbA1c;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;

import org.joda.time.DateTime;
import org.joda.time.Interval;

class HbA1cDashboardValue implements DashboardValue {
    
    private final String key;
    private final String value;
    private Entry entry;

    HbA1cDashboardValue(Context context) {
        Float userGenerated = forUserGeneratedHbA1c();
        if (userGenerated != null) {
            key = context.getString(R.string.hba1c_previous);
            value = print(context, userGenerated);
        } else {
            Float calculated = forCalculatedHbA1c();
            key = context.getString(R.string.hba1c_estimated);
            value = print(context, calculated);
        }
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Nullable
    @Override
    public Entry getEntry() {
        return entry;
    }

    @Nullable
    private Float forUserGeneratedHbA1c() {
        Entry latestHbA1cEntry = EntryDao.getInstance().getLatestWithMeasurement(HbA1c.class);
        boolean latestHbA1cEntryIsRecent = latestHbA1cEntry != null
            && latestHbA1cEntry.getDate().isAfter(DateTime.now().minusMonths(1));
        if (latestHbA1cEntryIsRecent) {
            latestHbA1cEntry.setMeasurementCache(EntryDao.getInstance().getMeasurements(latestHbA1cEntry));
            for (Measurement measurement : latestHbA1cEntry.getMeasurementCache()) {
                if (measurement instanceof HbA1c) {
                    this.entry = latestHbA1cEntry;
                    HbA1c hbA1c = (HbA1c) measurement;
                    return PreferenceStore.getInstance().formatDefaultToCustomUnit(
                        Category.HBA1C,
                        hbA1c.getValues()[0]
                    );
                }
            }
        }
        return null;
    }

    @NonNull
    private Float forCalculatedHbA1c() {
        DateTime now = DateTime.now();
        Interval intervalQuarter = new Interval(new DateTime(now.minusMonths(3)), now);
        float avgQuarter = MeasurementDao.getInstance(BloodSugar.class)
            .function(SqlFunction.AVG, BloodSugar.Column.MGDL, intervalQuarter);
        if (avgQuarter > 0) {
            return PreferenceStore.getInstance().formatDefaultToCustomUnit(
                Category.HBA1C,
                Helper.calculateHbA1c(avgQuarter)
            );
        } else {
            return 0f;
        }
    }

    private String print(Context context, float hbA1c) {
        return hbA1c > 0 ? String.format("%s %s",
            FloatUtils.parseFloat(hbA1c),
            PreferenceStore.getInstance().getUnitAcronym(Category.HBA1C)
        ) : context.getString(R.string.placeholder);
    }
}
