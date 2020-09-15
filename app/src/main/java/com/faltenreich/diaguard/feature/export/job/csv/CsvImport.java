package com.faltenreich.diaguard.feature.export.job.csv;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.feature.export.job.Export;
import com.faltenreich.diaguard.feature.export.job.ExportCallback;
import com.faltenreich.diaguard.feature.export.job.FileType;
import com.faltenreich.diaguard.feature.export.job.date.DateStrategy;
import com.faltenreich.diaguard.feature.export.job.date.OriginDateStrategy;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.database.DatabaseHelper;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.EntryTagDao;
import com.faltenreich.diaguard.shared.data.database.dao.FoodDao;
import com.faltenreich.diaguard.shared.data.database.dao.FoodEatenDao;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;
import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.data.database.entity.deprecated.CategoryDeprecated;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.opencsv.CSVReader;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class CsvImport extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = CsvImport.class.getSimpleName();
    private static final DateStrategy defaultDateStrategy = new OriginDateStrategy();

    @NonNull
    private InputStream inputStream;

    @Nullable
    private DateStrategy dateStrategy;

    @Nullable
    private ExportCallback callback;

    public CsvImport(@NonNull InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setCallback(@Nullable ExportCallback callback) {
        this.callback = callback;
    }

    public void setDateStrategy(@Nullable DateStrategy dateStrategy) {
        this.dateStrategy = dateStrategy;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream), CsvMeta.CSV_DELIMITER);
            String[] nextLine = reader.readNext();

            // First version was without meta information
            if (!nextLine[0].equals(CsvMeta.CSV_KEY_META)) {
                importFromVersion1_0(reader, nextLine);
            } else {
                int databaseVersion = Integer.parseInt(nextLine[1]);
                if (databaseVersion == DatabaseHelper.DATABASE_VERSION_1_1) {
                    importFromVersion1_1(reader, nextLine);
                } else if (databaseVersion <= DatabaseHelper.DATABASE_VERSION_2_2) {
                    importFromVersion2_2(reader, nextLine);
                } else {
                    importFromVersion3_0(reader, nextLine);
                }
            }
            reader.close();
            return true;

        } catch (Exception exception) {
            Log.e(TAG, exception.toString());
            return false;
        }
    }

    private void importFromVersion1_0(CSVReader reader, String[] nextLine) throws Exception {
        while (nextLine != null) {
            Entry entry = new Entry();
            entry.setDate(DateTimeFormat.forPattern(Export.BACKUP_DATE_FORMAT).parseDateTime(nextLine[1]));
            String note = nextLine[2];
            entry.setNote(note != null && note.length() > 0 ? note : null);
            EntryDao.getInstance().createOrUpdate(entry);
            try {
                CategoryDeprecated categoryDeprecated = Helper.valueOf(CategoryDeprecated.class, nextLine[2]);
                Category category = categoryDeprecated.toUpdate();
                Measurement measurement = category.toClass().newInstance();
                measurement.setValues(FloatUtils.parseNumber(nextLine[0]));
                measurement.setEntry(entry);
                MeasurementDao.getInstance(category.toClass()).createOrUpdate(measurement);
            } catch (InstantiationException exception) {
                Log.e(TAG, exception.toString());
            } catch (IllegalAccessException exception) {
                Log.e(TAG, exception.toString());
            }
            nextLine = reader.readNext();
        }
    }

    @SuppressWarnings("ParameterCanBeLocal")
    private void importFromVersion1_1(CSVReader reader, String[] nextLine) throws Exception {
        Entry entry = null;
        while ((nextLine = reader.readNext()) != null) {
            String key = nextLine[0];
            if (key.equalsIgnoreCase(Entry.BACKUP_KEY)) {
                entry = new Entry();
                entry.setDate(DateTimeFormat.forPattern(Export.BACKUP_DATE_FORMAT).parseDateTime(nextLine[1]));
                String note = nextLine[2];
                entry.setNote(note != null && note.length() > 0 ? note : null);
                entry = EntryDao.getInstance().createOrUpdate(entry);
            } else if (key.equalsIgnoreCase(Measurement.BACKUP_KEY) && entry != null) {
                try {
                    CategoryDeprecated categoryDeprecated = Helper.valueOf(CategoryDeprecated.class, nextLine[2]);
                    Category category = categoryDeprecated.toUpdate();
                    Measurement measurement = category.toClass().newInstance();
                    measurement.setValues(FloatUtils.parseNumber(nextLine[1]));
                    measurement.setEntry(entry);
                    MeasurementDao.getInstance(category.toClass()).createOrUpdate(measurement);
                } catch (InstantiationException exception) {
                    Log.e(TAG, exception.toString());
                } catch (IllegalAccessException exception) {
                    Log.e(TAG, exception.toString());
                }
            }
        }
    }

    @SuppressWarnings("ParameterCanBeLocal")
    private void importFromVersion2_2(CSVReader reader, String[] nextLine) throws Exception {
        Entry entry = null;
        while ((nextLine = reader.readNext()) != null) {
            String key = nextLine[0];
            if (key.equalsIgnoreCase(Entry.BACKUP_KEY)) {
                entry = new Entry();
                entry.setDate(DateTimeFormat.forPattern(Export.BACKUP_DATE_FORMAT).parseDateTime(nextLine[1]));
                String note = nextLine[2];
                entry.setNote(note != null && note.length() > 0 ? note : null);
                entry = EntryDao.getInstance().createOrUpdate(entry);
            } else if (key.equalsIgnoreCase(Measurement.BACKUP_KEY) && entry != null) {
                try {
                    Category category = Helper.valueOf(Category.class, nextLine[1]);
                    Measurement measurement = category.toClass().newInstance();

                    List<Float> valueList = new ArrayList<>();
                    for (int position = 2; position < nextLine.length; position++) {
                        String valueString = nextLine[position];
                        try {
                            valueList.add(FloatUtils.parseNumber(valueString));
                        } catch (NumberFormatException exception) {
                            Log.e(TAG, exception.toString());
                        }
                    }
                    float[] values = new float[valueList.size()];
                    for (int position = 0; position < valueList.size(); position++) {
                        values[position] = valueList.get(position);
                    }
                    measurement.setValues(values);
                    measurement.setEntry(entry);
                    MeasurementDao.getInstance(category.toClass()).createOrUpdate(measurement);
                } catch (InstantiationException exception) {
                    Log.e(TAG, exception.toString());
                } catch (IllegalAccessException exception) {
                    Log.e(TAG, exception.toString());
                }
            }
        }
    }

    @SuppressWarnings("ParameterCanBeLocal")
    private void importFromVersion3_0(CSVReader reader, String[] nextLine) throws Exception {
        Entry lastEntry = null;
        Meal lastMeal = null;
        while ((nextLine = reader.readNext()) != null) {
            switch (nextLine[0]) {
                case Tag.BACKUP_KEY:
                    if (nextLine.length >= 2) {
                        String tagName = nextLine[1];
                        if (TagDao.getInstance().getByName(tagName) == null) {
                            Tag tag = new Tag();
                            tag.setName(nextLine[1]);
                            TagDao.getInstance().createOrUpdate(tag);
                        }
                    }
                    break;
                case Food.BACKUP_KEY:
                    if (nextLine.length >= 5) {
                        String foodName = nextLine[1];
                        Food food = FoodDao.getInstance().get(foodName);
                        if (food == null) {
                            food = new Food();
                            food.setName(foodName);
                            food.setBrand(nextLine[2]);
                            food.setIngredients(nextLine[3]);
                            food.setCarbohydrates(FloatUtils.parseNumber(nextLine[4]));
                            FoodDao.getInstance().createOrUpdate(food);
                        } else if (food.isDeleted()) {
                            // Reactivate previously deleted food that is being re-imported
                            food.setDeletedAt(null);
                            FoodDao.getInstance().createOrUpdate(food);
                        }
                    }
                    break;
                case Entry.BACKUP_KEY:
                    lastMeal = null;
                    if (nextLine.length >= 3) {
                        DateTime parsedDateTime = DateTimeFormat.forPattern(Export.BACKUP_DATE_FORMAT).parseDateTime(nextLine[1]);
                        DateStrategy dateStrategy = this.dateStrategy != null ? this.dateStrategy : defaultDateStrategy;
                        DateTime dateTime = dateStrategy.convertDate(parsedDateTime);

                        lastEntry = new Entry();
                        lastEntry.setDate(dateTime);
                        String note = nextLine[2];
                        lastEntry.setNote(note != null && note.length() > 0 ? note : null);

                        lastEntry = EntryDao.getInstance().createOrUpdate(lastEntry);
                        break;
                    }
                case Measurement.BACKUP_KEY:
                    if (lastEntry != null && nextLine.length >= 3) {
                        Category category = Helper.valueOf(Category.class, nextLine[1]);
                        if (category != null) {
                            try {
                                Measurement measurement = category.toClass().newInstance();

                                List<Float> valueList = new ArrayList<>();
                                for (int position = 2; position < nextLine.length; position++) {
                                    String valueString = nextLine[position];
                                    try {
                                        valueList.add(FloatUtils.parseNumber(valueString));
                                    } catch (NumberFormatException exception) {
                                        Log.e(TAG, exception.toString());
                                    }
                                }
                                float[] values = new float[valueList.size()];
                                for (int position = 0; position < valueList.size(); position++) {
                                    values[position] = valueList.get(position);
                                }
                                measurement.setValues(values);
                                measurement.setEntry(lastEntry);
                                MeasurementDao.getInstance(category.toClass()).createOrUpdate(measurement);

                                if (measurement instanceof Meal) {
                                    lastMeal = (Meal) measurement;
                                }
                            } catch (InstantiationException exception) {
                                Log.e(TAG, exception.toString());
                            } catch (IllegalAccessException exception) {
                                Log.e(TAG, exception.toString());
                            }
                        }
                    }
                    break;
                case EntryTag.BACKUP_KEY:
                    if (lastEntry != null && nextLine.length >= 2) {
                        Tag tag = TagDao.getInstance().getByName(nextLine[1]);
                        if (tag != null) {
                            EntryTag entryTag = new EntryTag();
                            entryTag.setEntry(lastEntry);
                            entryTag.setTag(tag);
                            EntryTagDao.getInstance().createOrUpdate(entryTag);
                        }
                    }
                    break;
                case FoodEaten.BACKUP_KEY:
                    if (lastMeal != null && nextLine.length >= 3) {
                        Food food = FoodDao.getInstance().get(nextLine[1]);
                        if (food != null) {
                            FoodEaten foodEaten = new FoodEaten();
                            foodEaten.setMeal(lastMeal);
                            foodEaten.setFood(food);
                            foodEaten.setAmountInGrams(FloatUtils.parseNumber(nextLine[2]));
                            FoodEatenDao.getInstance().createOrUpdate(foodEaten);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (callback != null) {
            if (success) {
                callback.onSuccess(null, FileType.CSV.mimeType);
            } else {
                callback.onError();
            }
        }
    }
}
