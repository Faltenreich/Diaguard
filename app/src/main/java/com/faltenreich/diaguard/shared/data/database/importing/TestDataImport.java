package com.faltenreich.diaguard.shared.data.database.importing;

import android.util.Log;

import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;

import org.joda.time.DateTime;

class TestDataImport implements Importing {

    private static final String TAG = TagImport.class.getSimpleName();
    private static final int DATA_COUNT = 10000;

    @Override
    public boolean requiresImport() {
        return false;
    }

    @Override
    public void importData() {
        for (int count = 0; count < DATA_COUNT; count++) {
            DateTime dateTime = DateTime.now().minusHours(count);
            Entry entry = new Entry();
            entry.setDate(dateTime);
            EntryDao.getInstance().createOrUpdate(entry);

            BloodSugar bloodSugar = new BloodSugar();
            bloodSugar.setMgDl(120);
            bloodSugar.setEntry(entry);
            MeasurementDao.getInstance(BloodSugar.class).createOrUpdate(bloodSugar);

            Log.d(TAG, "Created test data: " + (count + 1) + "/" + DATA_COUNT);
        }
    }
}
