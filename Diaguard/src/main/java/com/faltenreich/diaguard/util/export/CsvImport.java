package com.faltenreich.diaguard.util.export;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.faltenreich.diaguard.data.DatabaseHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.FileUtils;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.IFileListener;
import com.opencsv.CSVReader;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Faltenreich on 21.10.2015.
 */
public class CsvImport<T extends Measurement> extends AsyncTask<Void, Void, Void> {

    private static final String TAG = CsvImport.class.getSimpleName();

    private Context context;
    private File file;
    private IFileListener listener;

    public CsvImport(Context context, File file) {
        this.context = context;
        this.file = file;
    }

    public void setListener(IFileListener listener) {
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
                    entry.setDate(Export.dateTimeFromCsv(nextLine[1]));
                    entry.setNote(nextLine[2]);
                    long entryId = EntryDao.getInstance().createOrUpdate(entry);
                    try {
                        Measurement.Category category = Measurement.Category.valueOf(nextLine[3]);
                        T measurement = (T) category.toClass().newInstance();
                        measurement.setValues(new float[]{Float.parseFloat(nextLine[0])});
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
                    long parentId = -1;
                    while ((nextLine = reader.readNext()) != null) {
                        String key = nextLine[0];
                        if (key.equals(DatabaseHelper.ENTRY)) {
                            Entry entry = new Entry();
                            entry.setDate(Export.dateTimeFromCsv(nextLine[1]));
                            entry.setNote(nextLine[2]);
                            parentId = EntryDao.getInstance().createOrUpdate(entry);
                        } else if (key.equals(DatabaseHelper.MEASUREMENT) && parentId != -1) {
                            try {
                                Measurement.Category category = Measurement.Category.valueOf(nextLine[2]);
                                T measurement = (T) category.toClass().newInstance();
                                measurement.setValues(new float[]{Float.parseFloat(nextLine[1])});
                                measurement.setEntry(EntryDao.getInstance().get(parentId));
                                MeasurementDao.getInstance(category.toClass()).createOrUpdate(measurement);
                            } catch (InstantiationException e) {
                                Log.e(TAG, e.getMessage());
                            } catch (IllegalAccessException e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                } else if (databaseVersion == DatabaseHelper.DATABASE_VERSION_1_3) {
                    // TODO
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
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}
