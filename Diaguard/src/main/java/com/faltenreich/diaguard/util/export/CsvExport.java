package com.faltenreich.diaguard.util.export;

import android.os.AsyncTask;
import android.util.Log;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.DatabaseHelper;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.FileUtils;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.IFileListener;
import com.opencsv.CSVWriter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 21.10.2015.
 */
public class CsvExport extends AsyncTask<Void, String, File> {

    private static final String TAG = CsvExport.class.getSimpleName();

    private boolean isBackup;
    private DateTime dateStart;
    private DateTime dateEnd;
    private Measurement.Category[] categories;
    private IFileListener listener;

    public CsvExport(boolean isBackup, DateTime dateStart, DateTime dateEnd, Measurement.Category[] categories) {
        this.isBackup = isBackup;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.categories = categories;
    }

    public void setListener(IFileListener listener) {
        this.listener = listener;
    }

    @Override
    protected File doInBackground(Void... params) {
        File file = isBackup?
                Export.getBackupFile(Export.FileType.CSV) :
                Export.getExportFile(Export.FileType.CSV);
        try {
            FileWriter fileWriter = new FileWriter(file);
            CSVWriter writer = new CSVWriter(fileWriter, Export.CSV_DELIMITER);

            // Meta information to detect the data scheme in future iterations
            if (isBackup) {
                String[] meta = new String[]{
                        Export.CSV_KEY_META,
                        Integer.toString(DatabaseHelper.getVersion())};
                writer.writeNext(meta);
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

                List<String> entryValues = new ArrayList<>();
                if (isBackup) {
                    entryValues.add(Entry.class.getSimpleName().toLowerCase());
                    entryValues.add(Helper.getDateTimeFormatExport().print(entry.getDate()));
                } else {
                    entryValues.add(String.format("%s %s",
                            Helper.getDateFormat().print(entry.getDate()),
                            Helper.getTimeFormat().print(entry.getDate())));
                }
                entryValues.add(entry.getNote());
                writer.writeNext(entryValues.toArray(new String[entryValues.size()]));

                List<Measurement> measurements = categories != null ?
                        EntryDao.getInstance().getMeasurements(entry, categories) :
                        EntryDao.getInstance().getMeasurements(entry);
                for (Measurement measurement : measurements) {
                    List<String> measurementValues = new ArrayList<>();
                    if (isBackup) {
                        measurementValues.add(Measurement.class.getSimpleName().toLowerCase());
                        measurementValues.add(measurement.getCategory().name().toLowerCase());
                        for (float value : measurement.getValues()) {
                            measurementValues.add(Float.toString(value));
                        }
                    } else {
                        measurementValues.add(measurement.getCategory().toString());
                        for (float value : measurement.getValues()) {
                            measurementValues.add(Float.toString(PreferenceHelper.getInstance().
                                    formatDefaultToCustomUnit(measurement.getCategory(), value)));
                        }
                    }
                    writer.writeNext(measurementValues.toArray(new String[measurementValues.size()]));
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
    protected void onPreExecute() {

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
        if (listener != null)
            listener.onComplete(file, Export.CSV_MIME_TYPE);
    }
}