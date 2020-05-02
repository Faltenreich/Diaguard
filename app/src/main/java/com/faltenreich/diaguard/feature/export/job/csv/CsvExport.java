package com.faltenreich.diaguard.feature.export.job.csv;

import android.os.AsyncTask;
import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.DatabaseHelper;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.EntryTagDao;
import com.faltenreich.diaguard.shared.data.database.dao.FoodDao;
import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.feature.export.job.Export;
import com.faltenreich.diaguard.feature.export.job.ExportCallback;
import com.faltenreich.diaguard.feature.export.job.FileType;
import com.opencsv.CSVWriter;

import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvExport extends AsyncTask<Void, String, File> {

    private static final String TAG = CsvExport.class.getSimpleName();

    private CsvExportConfig config;

    public CsvExport(CsvExportConfig config) {
        this.config = config;
    }

    private ExportCallback getCallback() {
        return config.getCallback();
    }

    @Override
    protected File doInBackground(Void... params) {
        DateTime dateStart = config.getDateStart();
        DateTime dateEnd = config.getDateEnd();
        Category[] categories = config.getCategories();
        boolean isBackup = config.isBackup();

        File file = isBackup?
                Export.getBackupFile(config, FileType.CSV) :
                Export.getExportFile(config);
        try {
            FileWriter fileWriter = new FileWriter(file);
            CSVWriter writer = new CSVWriter(fileWriter, CsvMeta.CSV_DELIMITER);

            if (isBackup) {
                // Meta information to detect the data scheme in future iterations
                String[] meta = new String[]{
                    CsvMeta.CSV_KEY_META,
                        Integer.toString(DatabaseHelper.getVersion())};
                writer.writeNext(meta);

                List<Tag> tags = TagDao.getInstance().getAll();
                for (Tag tag : tags) {
                    writer.writeNext(ArrayUtils.add(tag.getValuesForBackup(), 0, tag.getKeyForBackup()));
                }

                List<Food> foods = FoodDao.getInstance().getAllFromUser();
                for (Food food: foods) {
                    writer.writeNext(ArrayUtils.add(food.getValuesForBackup(), 0, food.getKeyForBackup()));
                }
            }

            List<Entry> entries = dateStart != null && dateEnd != null ?
                    EntryDao.getInstance().getEntriesBetween(dateStart, dateEnd) :
                    EntryDao.getInstance().getAll();
            int position = 0;
            for (Entry entry : entries) {
                publishProgress(String.format("%s %d/%d",
                        config.getContext().getString(R.string.entry),
                        position,
                        entries.size()));

                writer.writeNext(isBackup ? ArrayUtils.add(entry.getValuesForBackup(), 0, entry.getKeyForBackup()) : entry.getValuesForExport());

                List<Measurement> measurements = categories != null ? EntryDao.getInstance().getMeasurements(entry, categories) : EntryDao.getInstance().getMeasurements(entry);
                for (Measurement measurement : measurements) {
                    writer.writeNext(isBackup ? ArrayUtils.add(measurement.getValuesForBackup(), 0, measurement.getKeyForBackup()) : measurement.getValuesForExport());

                    if (isBackup && measurement instanceof Meal) {
                        Meal meal = (Meal) measurement;
                        for (FoodEaten foodEaten : meal.getFoodEaten()) {
                            writer.writeNext(ArrayUtils.add(foodEaten.getValuesForBackup(), 0, foodEaten.getKeyForBackup()));
                        }
                    }
                }

                if (isBackup) {
                    List<EntryTag> entryTags = EntryTagDao.getInstance().getAll(entry);
                    for (EntryTag entryTag : entryTags) {
                        writer.writeNext(ArrayUtils.add(entryTag.getValuesForBackup(), 0, entryTag.getKeyForBackup()));
                    }
                }

                position++;
            }

            writer.close();
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }

        return file;
    }

    @Override
    protected void onProgressUpdate(String... message) {
        if (getCallback() != null) {
            getCallback().onProgress(message[0]);
        }
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if (getCallback() != null) {
            if (file != null) {
                getCallback().onSuccess(file, FileType.CSV.mimeType);
            } else {
                getCallback().onError();
            }
        }
    }
}