package com.faltenreich.diaguard.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.faltenreich.diaguard.database.DatabaseDataSource;
import com.faltenreich.diaguard.database.DatabaseHelper;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.Measurement;
import com.faltenreich.diaguard.database.Model;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * Created by Filip on 05.06.2014.
 */
public class FileHelper {
    public static final String PATH_EXTERNAL = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String PATH_EXTERNAL_PARENT = Environment.getExternalStorageDirectory().getParent();
    public static final String PATH_STORAGE =  File.separator  + "Diaguard";

    public static final char DELIMITER = ';';
    public static final String KEY_META = "meta";

    public static final String MIME_MAIL = "message/rfc822";
    public static final String MIME_PDF = "application/pdf";
    public static final String MIME_CSV = "text/csv";

    Context context;
    DatabaseDataSource dataSource;
    PreferenceHelper preferenceHelper;

    public FileHelper(Context context) {
        this.context = context;
        this.dataSource = new DatabaseDataSource(context);
        this.preferenceHelper = new PreferenceHelper(context);
    }

    private static boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static File getExternalStorage() {

        if(!isExternalStorageWritable())
            return null;

        String path = null;

        if(new File(FileHelper.PATH_EXTERNAL).exists())
            path = PATH_EXTERNAL;

        else if(new File(FileHelper.PATH_EXTERNAL_PARENT).exists())
            path = PATH_EXTERNAL_PARENT;

        if(path == null)
            return null;

        File file = new File(path + PATH_STORAGE);
        boolean fileCouldBeCreated = true;

        if(!file.exists())
            fileCouldBeCreated = file.mkdirs();

        if(!fileCouldBeCreated)
            return null;

        return file;
    }

    public File exportCSV() {
        CSVExportTask csvExportTask = new CSVExportTask();
        csvExportTask.execute();

        File createdFile = null;
        try {
            createdFile = csvExportTask.get();
        }
        catch(InterruptedException ex) {
            ex.printStackTrace();
        }
        catch(ExecutionException ex) {
            ex.printStackTrace();
        }
        return createdFile;
    }

    public void importCSV(String fileName) {
        CSVImportTask csvImportTask = new CSVImportTask();
        csvImportTask.execute(fileName);
    }

    private class CSVExportTask extends AsyncTask<Void, Void, File> {

        @Override
        protected File doInBackground(Void... params) {

            File directory = FileHelper.getExternalStorage();
            if(directory == null)
                return null;

            File file = new File(directory.getAbsolutePath() + "/backup" + DateTimeFormat.forPattern("yyyyMMddHHmmss").
                    print(new DateTime()) + ".csv");

            dataSource.open();

            try {
                FileWriter fileWriter = new FileWriter(file);
                CSVWriter writer = new CSVWriter(fileWriter, DELIMITER);

                // Meta information to detect the database schema in future iterations
                String[] meta = new String[]{
                        KEY_META,
                        Integer.toString(dataSource.getVersion()) };
                writer.writeNext(meta);

                List<Model> entries = dataSource.get(DatabaseHelper.ENTRY);
                for(Model entryModel : entries) {
                    Entry entry = (Entry)entryModel;
                    String[] entryValues = {
                            DatabaseHelper.ENTRY,
                            Helper.getDateDatabaseFormat().print(entry.getDate()),
                            entry.getNote() };
                    writer.writeNext(entryValues);

                    List<Model> measurements = dataSource.get(DatabaseHelper.MEASUREMENT, null,
                            DatabaseHelper.ENTRY_ID + "=?",
                            new String[]{Long.toString(entry.getId())},
                            null, null, null, null);
                    for(Model measurementModel : measurements) {
                        Measurement measurement = (Measurement)measurementModel;
                        String[] measurementValues = {
                                DatabaseHelper.MEASUREMENT,
                                Float.toString(measurement.getValue()),
                                measurement.getCategory().name()
                        };
                        writer.writeNext(measurementValues);
                    }
                }

                writer.close();
            }
            catch (IOException ex) {
                //Log.e("DiaguardError", ex.getMessage());
            }

            dataSource.close();

            return directory;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private class CSVImportTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                String filePath = getExternalStorage().getAbsolutePath() + File.separator + params[0];
                CSVReader reader = new CSVReader(new FileReader(filePath), DELIMITER);

                dataSource.open();

                // Read first line and check database version
                String[] nextLine = reader.readNext();

                if(!nextLine[0].equals(KEY_META)) {
                    // First database version without meta information (17)
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
                        measurement.setEntryId(entryId);
                        dataSource.insert(measurement);

                        nextLine = reader.readNext();
                    }
                }

                // Database version > 17
                else {
                    int databaseVersion = Integer.parseInt(nextLine[1]);

                    // Migrate from old database version
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
                            Measurement measurement = new Measurement();
                            measurement.setValue(Float.parseFloat(nextLine[1]));
                            measurement.setCategory(Measurement.Category.valueOf(nextLine[2]));
                            measurement.setEntryId(parentId);
                            dataSource.insert(measurement);
                        }
                    }
                }

                dataSource.close();
                reader.close();

            } catch (IOException ex) {
                //Log.e("DiaguardError", ex.getMessage());
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
