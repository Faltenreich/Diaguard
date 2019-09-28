package com.faltenreich.diaguard.util.export;

import android.os.AsyncTask;
import android.util.Log;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.DatabaseHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.EntryTagDao;
import com.faltenreich.diaguard.data.dao.FoodDao;
import com.faltenreich.diaguard.data.dao.TagDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.entity.Tag;
import com.opencsv.CSVWriter;

import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvExport extends AsyncTask<Void, String, File> {

    private static final String TAG = CsvExport.class.getSimpleName();

    private ExportConfig config;
    private boolean isBackup;
    private FileListener listener;

    CsvExport(ExportConfig config, boolean isBackup) {
        this.config = config;
        this.isBackup = isBackup;
    }

    public void setListener(FileListener listener) {
        this.listener = listener;
    }

    @Override
    protected File doInBackground(Void... params) {
        DateTime dateStart = config.getDateStart();
        DateTime dateEnd = config.getDateEnd();
        Measurement.Category[] categories = config.getCategories();

        File file = isBackup?
                Export.getBackupFile(Export.FileType.CSV) :
                Export.getExportFile(Export.FileType.CSV);
        try {
            FileWriter fileWriter = new FileWriter(file);
            CSVWriter writer = new CSVWriter(fileWriter, Export.CSV_DELIMITER);

            if (isBackup) {
                // Meta information to detect the data scheme in future iterations
                String[] meta = new String[]{
                        Export.CSV_KEY_META,
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
                        DiaguardApplication.getContext().getString(R.string.entry),
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
        if (listener != null) {
            listener.onProgress(message[0]);
        }
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if (listener != null) {
            if (file != null) {
                listener.onSuccess(file, Export.CSV_MIME_TYPE);
            } else {
                listener.onError();
            }
        }
    }
}