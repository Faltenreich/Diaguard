package com.faltenreich.diaguard.util.export;

import android.os.AsyncTask;
import android.util.Log;

import com.faltenreich.diaguard.data.DatabaseHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.EntryTagDao;
import com.faltenreich.diaguard.data.dao.FoodDao;
import com.faltenreich.diaguard.data.dao.FoodEatenDao;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.data.dao.TagDao;
import com.faltenreich.diaguard.data.entity.EntryTag;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Tag;
import com.faltenreich.diaguard.data.entity.deprecated.CategoryDeprecated;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.NumberUtils;
import com.opencsv.CSVReader;

import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvImport extends AsyncTask<Void, Void, Void> {

    private static final String TAG = CsvImport.class.getSimpleName();

    private File file;
    private FileListener listener;

    CsvImport(File file) {
        this.file = file;
    }

    public void setListener(FileListener listener) {
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            CSVReader reader = new CSVReader(new FileReader(file), Export.CSV_DELIMITER);
            String[] nextLine = reader.readNext();

            // First version without meta information (1.0)
            if (!nextLine[0].equals(Export.CSV_KEY_META)) {
                while (nextLine != null) {
                    Entry entry = new Entry();
                    entry.setDate(DateTimeFormat.forPattern(Export.BACKUP_DATE_FORMAT).parseDateTime(nextLine[1]));
                    String note = nextLine[2];
                    entry.setNote(note != null && note.length() > 0 ? note : null);
                    EntryDao.getInstance().createOrUpdate(entry);
                    try {
                        CategoryDeprecated categoryDeprecated = Helper.valueOf(CategoryDeprecated.class, nextLine[2]);
                        Measurement.Category category = categoryDeprecated.toUpdate();
                        Measurement measurement = (Measurement) category.toClass().newInstance();
                        measurement.setValues(NumberUtils.parseNumber(nextLine[0]));
                        measurement.setEntry(entry);
                        MeasurementDao.getInstance(category.toClass()).createOrUpdate(measurement);
                    } catch (InstantiationException e) {
                        Log.e(TAG, e.getMessage());
                    } catch (IllegalAccessException e) {
                        Log.e(TAG, e.getMessage());
                    }
                    nextLine = reader.readNext();
                }
            }

            // 1.1 or later
            else {
                int databaseVersion = Integer.parseInt(nextLine[1]);
                if (databaseVersion == DatabaseHelper.DATABASE_VERSION_1_1) {
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
                                Measurement.Category category = categoryDeprecated.toUpdate();
                                Measurement measurement = category.toClass().newInstance();
                                measurement.setValues(new float[]{NumberUtils.parseNumber(nextLine[1])});
                                measurement.setEntry(entry);
                                MeasurementDao.getInstance(category.toClass()).createOrUpdate(measurement);
                            } catch (InstantiationException e) {
                                Log.e(TAG, e.getMessage());
                            } catch (IllegalAccessException e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                } else if (databaseVersion <= DatabaseHelper.DATABASE_VERSION_2_2) {
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
                                Measurement.Category category = Helper.valueOf(Measurement.Category.class, nextLine[1]);
                                Measurement measurement = category.toClass().newInstance();

                                List<Float> valueList = new ArrayList<>();
                                for (int position = 2; position < nextLine.length; position++) {
                                    String valueString = nextLine[position];
                                    try {
                                        valueList.add(NumberUtils.parseNumber(valueString));
                                    } catch (NumberFormatException e) {
                                        Log.e(TAG, e.getMessage());
                                    }
                                }
                                float[] values = new float[valueList.size()];
                                for (int position = 0; position < valueList.size(); position++) {
                                    values[position] = valueList.get(position);
                                }
                                measurement.setValues(values);
                                measurement.setEntry(entry);
                                MeasurementDao.getInstance(category.toClass()).createOrUpdate(measurement);
                            } catch (InstantiationException e) {
                                Log.e(TAG, e.getMessage());
                            } catch (IllegalAccessException e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                } else if (databaseVersion <= DatabaseHelper.DATABASE_VERSION_2_5) {
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
                                    if (FoodDao.getInstance().get(foodName) == null) {
                                        Food food = new Food();
                                        food.setName(foodName);
                                        food.setBrand(nextLine[2]);
                                        food.setIngredients(nextLine[3]);
                                        food.setCarbohydrates(NumberUtils.parseNumber(nextLine[4]));
                                        FoodDao.getInstance().createOrUpdate(food);
                                    }
                                }
                                break;
                            case Entry.BACKUP_KEY:
                                lastMeal = null;
                                if (nextLine.length >= 3) {
                                    lastEntry = new Entry();
                                    lastEntry.setDate(DateTimeFormat.forPattern(Export.BACKUP_DATE_FORMAT).parseDateTime(nextLine[1]));
                                    String note = nextLine[2];
                                    lastEntry.setNote(note != null && note.length() > 0 ? note : null);
                                    lastEntry = EntryDao.getInstance().createOrUpdate(lastEntry);
                                    break;
                                }
                            case Measurement.BACKUP_KEY:
                                if (lastEntry != null && nextLine.length >= 3) {
                                    Measurement.Category category = Helper.valueOf(Measurement.Category.class, nextLine[1]);
                                    if (category != null) {
                                        try {
                                            Measurement measurement = category.toClass().newInstance();

                                            List<Float> valueList = new ArrayList<>();
                                            for (int position = 2; position < nextLine.length; position++) {
                                                String valueString = nextLine[position];
                                                try {
                                                    valueList.add(NumberUtils.parseNumber(valueString));
                                                } catch (NumberFormatException e) {
                                                    Log.e(TAG, e.getMessage());
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
                                        } catch (InstantiationException e) {
                                            Log.e(TAG, e.getMessage());
                                        } catch (IllegalAccessException e) {
                                            Log.e(TAG, e.getMessage());
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
                                        foodEaten.setAmountInGrams(NumberUtils.parseNumber(nextLine[2]));
                                        FoodEatenDao.getInstance().createOrUpdate(foodEaten);
                                    }
                                }
                                break;
                        }
                    }
                }
                else {
                    Log.e(TAG, "Unsupported database version: " + databaseVersion);
                }
            }
            reader.close();
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (listener != null) {
            listener.onComplete(file, Export.CSV_MIME_TYPE);
        }
    }
}
