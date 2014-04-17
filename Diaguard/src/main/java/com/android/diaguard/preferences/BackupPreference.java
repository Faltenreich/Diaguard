package com.android.diaguard.preferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;

import com.android.diaguard.R;
import com.android.diaguard.database.DatabaseDataSource;
import com.android.diaguard.database.Event;
import com.android.diaguard.helpers.Helper;
import com.android.diaguard.helpers.PreferenceHelper;
import com.android.diaguard.helpers.ViewHelper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * Created by Filip on 04.11.13.
 */
public class BackupPreference extends DialogPreference {

    private final int ACTION_CREATEBACKUP = 0;
    private final int ACTION_RESTOREBACKUP = 1;

    DatabaseDataSource dataSource;
    PreferenceHelper preferenceHelper;
    Activity activity;

    public BackupPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (Activity) context;

        dataSource = new DatabaseDataSource(activity);
        preferenceHelper = new PreferenceHelper(activity);

        // Hide standard buttons
        setPositiveButtonText(null);
        setNegativeButtonText(null);
    }

    @Override
    protected void onPrepareDialogBuilder(android.app.AlertDialog.Builder builder) {

        builder.setTitle(activity.getResources().getString(R.string.backup))
                .setItems(R.array.backup_actions, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case ACTION_CREATEBACKUP:
                                createBackup();
                                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                                String path = Helper.PATH_STORAGE + "/backup" +
                                        format.format(Calendar.getInstance().getTime()) + ".csv";
                                ViewHelper.showToastMessage(activity,
                                        activity.getResources().getString(R.string.pref_data_backup_finished) + ": " + path);
                                break;
                            case ACTION_RESTOREBACKUP:
                                dialog.dismiss();
                                showBackups();
                                break;
                            default:
                                dialog.dismiss();
                                break;
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
    }

    private void showBackups() {

        File path = new File(Helper.PATH_STORAGE);
        File[] files = path.listFiles();
        List<String> csvFiles = new ArrayList<String>();
        for (File file : files) {
            String fileName = file.getName();
            String extension = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
            if(extension.equals("csv"))
                csvFiles.add(fileName);
        }

        final String[] csvArray = csvFiles.toArray(new String[csvFiles.size()]);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

        final String[] csvArrayDates = new String[csvArray.length];
        for(int position = 0; position < csvArray.length; position++) {
            String dateString = csvArray[position].substring(6, csvArray[position].lastIndexOf("."));
            try {
                Calendar date = Calendar.getInstance();
                date.setTime(format.parse(dateString));
                csvArrayDates[position] = preferenceHelper.getDateAndTimeFormat().format(date.getTime());
            }
            catch (ParseException ex) {
                Log.e("BackupPreference", ex.getMessage());
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.backupchoose)
                .setItems(csvArrayDates, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        CSVImportTask csvImportTask = new CSVImportTask();
                        csvImportTask.execute(csvArray[which]);

                        ViewHelper.showToastMessage(activity,
                                activity.getResources().getString(R.string.pref_data_backup_import));
                    }
                }); 
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void createBackup() {
        CSVExportTask csvExport = new CSVExportTask();
        csvExport.execute();
    }

    private class CSVExportTask extends AsyncTask<Void, Void, List<Event>> {

        @Override
        protected List<Event> doInBackground(Void... params) {

            dataSource.open();
            List<Event> events = dataSource.getEvents();
            dataSource.close();

            return events;
        }

        @Override
        protected void onPostExecute(List<Event> events) {

            File directory = new File(Helper.PATH_STORAGE);

            if (directory != null)
                directory.mkdirs();

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            File file = new File(directory + "/backup" +
                    format.format(Calendar.getInstance().getTime()) + ".csv");

            try {
                FileWriter fileWriter = new FileWriter(file);
                CSVWriter writer = new CSVWriter(fileWriter);

                for(Event event : events) {
                    String[] array = {
                            Float.toString(event.getValue()),
                            Helper.getDateDatabaseFormat().format(event.getDate().getTime()),
                            event.getNotes(),
                            event.getCategory().name() };
                    writer.writeNext(array);
                }

                writer.close();
            }
            catch (IOException ex) {
                Log.e("DiaguardError", ex.getMessage());
            }
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
                String filePath = Helper.PATH_STORAGE + "/" + params[0];
                CSVReader reader = new CSVReader(new FileReader(filePath));

                Event event = new Event();
                String[] nextLine;
                dataSource.open();
                while((nextLine = reader.readNext()) != null) {

                    event.setValue(Float.parseFloat(nextLine[0]));
                    event.setDate(nextLine[1]);
                    event.setNotes(nextLine[2]);
                    event.setCategory(Event.Category.valueOf(nextLine[3]));
                    dataSource.insertEvent(event);
                }
                dataSource.close();
                reader.close();
            }
            catch (IOException ex) {
                Log.e("DiaguardError", ex.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {}

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}