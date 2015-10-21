package com.faltenreich.diaguard.util.export;

import android.content.Context;
import android.os.AsyncTask;

import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.FileUtils;
import com.faltenreich.diaguard.util.IFileListener;
import com.opencsv.CSVReader;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Faltenreich on 21.10.2015.
 */
public class CsvImport extends AsyncTask<Void, Void, Void> {

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

            // Read first line and check data version
            String[] nextLine = reader.readNext();

            // TODO
                        /*
                if(!nextLine[0].equals(CSV_KEY_META)) {
                    // First data version without meta information (17)
                    while (nextLine != null) {
                        // Entry
                        Entry entry = new Entry();
                        entry.setDate(nextLine[1]);
                        entry.setNote(nextLine[2]);
                        long entryId = dataSource.insert(entry);

                        // Measurement
                        Measurement measurement = new Measurement();
                        measurement.setValue(Float.parseFloat(nextLine[0]));
                        measurement.setCategory(Measurement.Category.valueOf(nextLine[3]));
                        measurement.setEntry(entryId);
                        dataSource.insert(measurement);

                        nextLine = reader.readNext();
                    }
                }

                // Database version > 17
                else {
                    int databaseVersion = Integer.parseInt(nextLine[1]);

                    // Migrate from old data version
                    if(databaseVersion < dataSource.getVersion()) {
                        // For future releases
                    }

                    long parentId = -1;
                    while ((nextLine = reader.readNext()) != null) {
                        String key = nextLine[0];
                        if (key.equals(DatabaseHelper.ENTRY)) {
                            Entry entry = new Entry();
                            entry.setDate(nextLine[1]);
                            entry.setNote(nextLine[2]);
                            parentId = dataSource.insert(entry);
                        }
                        else if(key.equals(DatabaseHelper.MEASUREMENT) && parentId != -1) {
                            // Measurement
                            // TODO
                            /*
                            Measurement measurement = new Measurement();
                            measurement.setValue(Float.parseFloat(nextLine[1]));
                            measurement.setCategory(Measurement.Category.valueOf(nextLine[2]));
                            measurement.setEntry(parentId);
                            dataSource.insert(measurement);
                        }
                    }
                }
                */

            reader.close();

        } catch (IOException ex) {
            //Log.e("DiaguardError", ex.getEntry());
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
