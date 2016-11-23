package com.faltenreich.diaguard.util.export;

import android.os.AsyncTask;
import android.util.Log;

import com.faltenreich.diaguard.data.DatabaseHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
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

/**
 * Created by Faltenreich on 21.10.2015.
 */
public class CsvImport extends AsyncTask<Void, Void, Void> {

    private static final String TAG = CsvImport.class.getSimpleName();

    private File file;
    private FileListener listener;

    public CsvImport(File file) {
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
                        measurement.setValues(new float[]{NumberUtils.parseNumber(nextLine[0])});
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
                        if (key.equalsIgnoreCase(Entry.class.getSimpleName())) {
                            entry = new Entry();
                            entry.setDate(DateTimeFormat.forPattern(Export.BACKUP_DATE_FORMAT).parseDateTime(nextLine[1]));
                            String note = nextLine[2];
                            entry.setNote(note != null && note.length() > 0 ? note : null);
                            entry = EntryDao.getInstance().createOrUpdate(entry);
                        } else if (key.equalsIgnoreCase(Measurement.class.getSimpleName()) && entry != null) {
                            try {
                                CategoryDeprecated categoryDeprecated = Helper.valueOf(CategoryDeprecated.class, nextLine[2]);
                                Measurement.Category category = categoryDeprecated.toUpdate();
                                Measurement measurement = (Measurement) category.toClass().newInstance();
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
                } else if (databaseVersion >= DatabaseHelper.DATABASE_VERSION_1_3) {
                    Entry entry = null;
                    while ((nextLine = reader.readNext()) != null) {
                        String key = nextLine[0];
                        if (key.equalsIgnoreCase(Entry.class.getSimpleName())) {
                            entry = new Entry();
                            entry.setDate(DateTimeFormat.forPattern(Export.BACKUP_DATE_FORMAT).parseDateTime(nextLine[1]));
                            String note = nextLine[2];
                            entry.setNote(note != null && note.length() > 0 ? note : null);
                            entry = EntryDao.getInstance().createOrUpdate(entry);
                        } else if (key.equalsIgnoreCase(Measurement.class.getSimpleName()) && entry != null) {
                            try {
                                Measurement.Category category = Helper.valueOf(Measurement.Category.class, nextLine[1]);
                                Measurement measurement = (Measurement) category.toClass().newInstance();

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
