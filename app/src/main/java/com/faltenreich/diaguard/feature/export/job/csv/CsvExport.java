package com.faltenreich.diaguard.feature.export.job.csv;

import android.os.AsyncTask;
import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.export.job.Export;
import com.faltenreich.diaguard.feature.export.job.ExportCallback;
import com.faltenreich.diaguard.feature.export.job.FileType;
import com.faltenreich.diaguard.shared.Helper;
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
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CsvExport extends AsyncTask<Void, String, File> {

    private static final String TAG = CsvExport.class.getSimpleName();

    private final CsvExportConfig config;

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

        File file = isBackup ?
            Export.getBackupFile(config, FileType.CSV) :
            Export.getExportFile(config);
        try {
            ICSVWriter writer = new CSVWriterBuilder(new FileWriter(file))
                .withSeparator(CsvMeta.CSV_DELIMITER)
                .build();

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
                for (Food food : foods) {
                    writer.writeNext(ArrayUtils.add(food.getValuesForBackup(), 0, food.getKeyForBackup()));
                }
            }

            List<Entry> entries = dateStart != null && dateEnd != null ?
                EntryDao.getInstance().getEntriesBetween(dateStart, dateEnd) :
                EntryDao.getInstance().getAll();
            int position = 0;
            for (Entry entry : entries) {
                publishProgress(String.format(Locale.getDefault(), "%s %d/%d",
                    config.getContext().getString(R.string.entry),
                    position,
                    entries.size())
                );

                if (isBackup) {
                    writer.writeNext(ArrayUtils.add(entry.getValuesForBackup(), 0, entry.getKeyForBackup()));
                } else {
                    String dateTime = String.format("%s %s",
                        Helper.getDateFormat().print(entry.getDate()),
                        Helper.getTimeFormat().print(entry.getDate())
                    ).toLowerCase();
                    writer.writeNext(new String[] { dateTime});

                    if (config.exportNotes() && !StringUtils.isBlank(entry.getNote())) {
                        writer.writeNext(new String[] {
                            config.getContext().getString(R.string.note),
                            entry.getNote()
                        });
                    }
                    if (config.exportTags()) {
                        List<String> tags = new ArrayList<>();
                        for (EntryTag entryTag : EntryTagDao.getInstance().getAll(entry)) {
                            Tag tag = entryTag.getTag();
                            if (tag != null) {
                                tags.addAll(Arrays.asList(entryTag.getValuesForExport(config.getContext())));
                            }
                        }
                        if (!tags.isEmpty()) {
                            writer.writeNext(new String[]{
                                config.getContext().getString(R.string.tags),
                                StringUtils.join(tags, ", ")
                            });
                        }
                    }
                }

                List<Measurement> measurements = categories != null ?
                    EntryDao.getInstance().getMeasurements(entry, categories) :
                    EntryDao.getInstance().getMeasurements(entry);
                for (Measurement measurement : measurements) {
                    writer.writeNext(isBackup
                        ? ArrayUtils.add(measurement.getValuesForBackup(), 0, measurement.getKeyForBackup())
                        : measurement.getValuesForExport(config.getContext())
                    );

                    if (isBackup && measurement instanceof Meal) {
                        Meal meal = (Meal) measurement;
                        for (FoodEaten foodEaten : meal.getFoodEaten()) {
                            if (foodEaten.getMeal() != null && foodEaten.getFood() != null) {
                                writer.writeNext(ArrayUtils.add(foodEaten.getValuesForBackup(), 0, foodEaten.getKeyForBackup()));
                            }
                        }
                    }
                }

                if (isBackup) {
                    List<EntryTag> entryTags = EntryTagDao.getInstance().getAll(entry);
                    for (EntryTag entryTag : entryTags) {
                        if (entryTag.getEntry() != null && entryTag.getTag() != null) {
                            writer.writeNext(ArrayUtils.add(entryTag.getValuesForBackup(), 0, entryTag.getKeyForBackup()));
                        }
                    }
                }

                position++;
            }

            writer.close();
        } catch (IOException exception) {
            Log.e(TAG, exception.toString());
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
                getCallback().onError(config.getContext().getString(R.string.error_unexpected));
            }
        }
    }
}