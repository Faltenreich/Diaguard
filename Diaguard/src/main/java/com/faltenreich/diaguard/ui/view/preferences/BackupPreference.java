package com.faltenreich.diaguard.ui.view.preferences;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.util.FileUtils;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.IFileListener;
import com.faltenreich.diaguard.util.ViewHelper;
import com.faltenreich.diaguard.util.export.Export;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 04.11.13.
 */
public class BackupPreference extends DialogPreference implements IFileListener {

    private final int ACTION_CREATEBACKUP = 0;
    private final int ACTION_RESTOREBACKUP = 1;

    private ProgressDialog progressDialog;

    public BackupPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        progressDialog = new ProgressDialog(getContext());

        // Hide standard buttons
        setPositiveButtonText(null);
        setNegativeButtonText(null);
    }

    @Override
    protected void onPrepareDialogBuilder(android.app.AlertDialog.Builder builder) {
        builder.setTitle(getContext().getString(R.string.backup))
                .setItems(R.array.backup_actions, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case ACTION_CREATEBACKUP:
                                createBackup();
                                String path = FileUtils.PATH_EXTERNAL + "/backup" +
                                        DateTimeFormat.forPattern("yyyyMMddHHmmss").print(new DateTime()) + ".csv";
                                // TODO ViewHelper.showSnackbar(activity, activity.getResources().getString(R.string.pref_data_backup_finished) + ": " + path);
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
        File path = FileUtils.getStorageDirectory();
        File[] files = path.listFiles();
        List<String> csvFiles = new ArrayList<String>();
        for (File file : files) {
            String fileName = file.getName();
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if(extension.equals(Export.FileType.CSV.getExtension()))
                csvFiles.add(0, fileName);
        }

        if(csvFiles.size() <= 0) {
            String errorMessage = String.format("%s %s",
                    getContext().getString(R.string.error_no_backups),
                    FileUtils.getStorageDirectory());
            // TODO: ViewHelper.showSnackbar(getView(), errorMessage);
            return;
        }

        final String[] csvArray = csvFiles.toArray(new String[csvFiles.size()]);
        final String[] csvArrayDates = new String[csvArray.length];
        for(int position = 0; position < csvArray.length; position++) {
            String fileName = csvArray[position];
            String dateString = csvArray[position].substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf("."));
            DateTime date = DateTimeFormat.forPattern("yyyyMMddHHmmss").parseDateTime(dateString);
            csvArrayDates[position] = Helper.getDateFormat().print(date) + " " +
                    Helper.getTimeFormat().print(date);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.backup_title)
                .setItems(csvArrayDates, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        importBackup(csvArray[which]);
                        // TODO ViewHelper.showSnackbar(activity, activity.getResources().getString(R.string.pref_data_backup_import));
                    }
                }); 
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void importBackup(String fileName) {
        showProgressDialog();
        Export.importCsv(this, new File(FileUtils.getStorageDirectory() + fileName));
    }

    private void createBackup() {
        showProgressDialog();
        Export.exportCsv(this, true, null, null, null);
    }

    private void showProgressDialog() {
        progressDialog.setMessage(getContext().getString(R.string.export_progress));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onProgress(String message) {
        progressDialog.setMessage(message);
    }

    @Override
    public void onComplete(File file, String mimeType) {
        progressDialog.dismiss();
        // TODO
    }
}