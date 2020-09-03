package com.faltenreich.diaguard.shared.data.database.importing;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;

import org.joda.time.DateTime;

import java.util.Locale;

public class Importer {

    private static final String TAG = Importer.class.getSimpleName();

    public static void validateImports(Context context) {
        Locale locale = Helper.getLocale();
        new TagImport(context, locale).validateImport();
        new FoodImport(context, locale).validateImport();
    }

    public static void createTestData() {
        new CreateTestData().execute();
    }

    private static class CreateTestData extends AsyncTask<Void, Void, Void> {

        private static final int DATA_COUNT = 1000;

        @Override
        protected Void doInBackground(Void... params) {
            for (int count = 0; count < DATA_COUNT; count++) {
                DateTime dateTime = DateTime.now().minusDays(count);
                Entry entry = new Entry();
                entry.setDate(dateTime);
                entry.setNote("Test");
                EntryDao.getInstance().createOrUpdate(entry);

                BloodSugar bloodSugar = new BloodSugar();
                bloodSugar.setMgDl(100);
                bloodSugar.setEntry(entry);
                MeasurementDao.getInstance(BloodSugar.class).createOrUpdate(bloodSugar);

                Meal meal = new Meal();
                meal.setCarbohydrates(20);
                meal.setEntry(entry);
                MeasurementDao.getInstance(Meal.class).createOrUpdate(meal);

                Log.d(TAG, "Created test data: " + (count + 1) + "/" + DATA_COUNT);
            }
            return null;
        }
    }
}
