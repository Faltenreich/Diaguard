package com.faltenreich.diaguard.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Filip on 05.06.2014.
 */
public class FileHelper {

    public static final String PATH_EXTERNAL = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String PATH_EXTERNAL_PARENT = Environment.getExternalStorageDirectory().getParent();
    public static final String PATH_STORAGE = File.separator + "Diaguard";

    public static final char DELIMITER = ';';
    public static final String KEY_META = "meta";

    public static final String MIME_MAIL = "message/rfc822";
    public static final String MIME_PDF = "application/pdf";
    public static final String MIME_CSV = "text/csv";

    public FileHelper() {

    }

    public static File getDeprecatedStorageDirectory() {
        File directory = new File(PATH_EXTERNAL);
        if (!directory.exists()) {
            directory = new File(PATH_EXTERNAL_PARENT);
        }
        directory = new File(directory.getAbsolutePath() + PATH_STORAGE);
        directory.mkdirs();
        return directory;
    }

    public static File getStorageDirectory() {
        String path = android.os.Build.VERSION.SDK_INT >= 19 ?
                Environment.DIRECTORY_DOCUMENTS :
                Environment.DIRECTORY_DOWNLOADS;
        File directory = Environment.getExternalStoragePublicDirectory(path);
        directory.mkdir();
        return directory;
    }

    public void exportCSV(IFileListener listener) {
        CSVExportTask csvExportTask = new CSVExportTask(listener);
        csvExportTask.execute();
    }

    public void importCSV(String fileName) {
        CSVImportTask csvImportTask = new CSVImportTask();
        csvImportTask.execute(fileName);
    }

    private class CSVExportTask extends AsyncTask<Void, Void, File> {
        IFileListener listener;

        public CSVExportTask(IFileListener listener) {
            this.listener = listener;
        }

        @Override
        protected File doInBackground(Void... params) {
            File file = new File(getStorageDirectory() + "/backup" + DateTimeFormat.forPattern("yyyyMMddHHmmss").
                    print(new DateTime()) + ".csv");

            try {
                FileWriter fileWriter = new FileWriter(file);
                CSVWriter writer = new CSVWriter(fileWriter, DELIMITER);

                // TODO
                        /*
                // Meta information to detect the data schema in future iterations
                String[] meta = new String[]{
                        KEY_META,
                        Integer.toString(dataSource.getVersion()) };
                writer.writeNext(meta);

                List<BaseEntity> entries = dataSource.get(DatabaseHelper.ENTRY);
                for(BaseEntity entryModel : entries) {
                    Entry entry = (Entry)entryModel;
                    String[] entryValues = {
                            DatabaseHelper.ENTRY,
                            Helper.getDateDatabaseFormat().print(entry.getDate()),
                            entry.getNote() };
                    writer.writeNext(entryValues);

                    List<BaseEntity> measurements = dataSource.get(DatabaseHelper.MEASUREMENT, null,
                            DatabaseHelper.ENTRY + "=?",
                            new String[]{Long.toString(entry.getId())},
                            null, null, null, null);
                    for(BaseEntity measurementModel : measurements) {
                        Measurement measurement = (Measurement)measurementModel;
                        String[] measurementValues = {
                                DatabaseHelper.MEASUREMENT,
                                Float.toString(measurement.getValue()),
                                measurement.getCategory().name()
                        };
                        writer.writeNext(measurementValues);
                    }
                }
                        */

                writer.close();
            } catch (IOException ex) {
                //Log.e("DiaguardError", ex.getEntry());
            }

            return file;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (listener != null)
                listener.handleFile(file, MIME_CSV);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class CSVImportTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                String filePath = getStorageDirectory() + File.separator + params[0];
                CSVReader reader = new CSVReader(new FileReader(filePath), DELIMITER);

                // Read first line and check data version
                String[] nextLine = reader.readNext();

                // TODO
                        /*
                if(!nextLine[0].equals(KEY_META)) {
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
}
