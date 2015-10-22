package com.faltenreich.diaguard.util.export;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.DatabaseHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.ArrayUtils;
import com.faltenreich.diaguard.util.FileUtils;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.IFileListener;
import com.opencsv.CSVWriter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Faltenreich on 21.10.2015.
 */
public class CsvExport extends AsyncTask<Void, String, File> {

    private static final String TAG = CsvExport.class.getSimpleName();

    private Context context;
    private DateTime dateStart;
    private DateTime dateEnd;
    private Measurement.Category[] categories;
    private IFileListener listener;

    public CsvExport(Context context) {
        this.context = context;
    }

    public CsvExport(Context context, DateTime dateStart, DateTime dateEnd, Measurement.Category[] categories) {
        this.context = context;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.categories = categories;
    }

    public void setListener(IFileListener listener) {
        this.listener = listener;
    }

    @Override
    protected File doInBackground(Void... params) {
        String fileName = String.format("%s%sbackup%s.csv",
                FileUtils.getStorageDirectory(),
                File.separator,
                DateTimeFormat.forPattern("yyyyMMddHHmmss").print(new DateTime()));
        File file = new File(fileName);

        try {
            FileWriter fileWriter = new FileWriter(file);
            CSVWriter writer = new CSVWriter(fileWriter, Export.CSV_DELIMITER);

            // Meta information to detect the data scheme in future iterations
            String[] meta = new String[]{
                    Export.CSV_KEY_META,
                    Integer.toString(DatabaseHelper.getVersion())};
            writer.writeNext(meta);

            int position = 0;
            List<Entry> entries = EntryDao.getInstance().getEntriesBetween(dateStart, dateEnd);
            for (Entry entry : entries) {
                publishProgress(String.format("%s %d/%d",
                        context.getString(R.string.entry),
                        position,
                        entries.size()));

                String[] entryValues = {
                        DatabaseHelper.ENTRY,
                        Helper.getDateTimeFormatExport().print(entry.getDate()),
                        entry.getNote()};
                writer.writeNext(entryValues);

                List<Measurement> measurements = EntryDao.getInstance().getMeasurements(entry, categories);
                for (Measurement measurement : measurements) {
                    // TODO: Split values
                    String[] measurementValues = {
                            DatabaseHelper.MEASUREMENT,
                            measurement.getCategory().name().toLowerCase(),
                            Float.toString(ArrayUtils.sum(measurement.getValues()))
                    };
                    writer.writeNext(measurementValues);
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