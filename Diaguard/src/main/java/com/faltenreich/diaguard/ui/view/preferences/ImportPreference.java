package com.faltenreich.diaguard.ui.view.preferences;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

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
public class ImportPreference extends DialogPreference implements IFileListener {

    private static final String TAG = ImportPreference.class.getSimpleName();

    private ProgressDialog progressDialog;

    public ImportPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        progressDialog = new ProgressDialog(getContext());

        // Hide standard buttons
        setPositiveButtonText(null);
        setNegativeButtonText(null);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        final List<File> backupFiles = Export.getBackupFiles();
        if(backupFiles.size() <= 0) {
            String errorMessage = String.format("%s %s",
                    getContext().getString(R.string.error_no_backups),
                    FileUtils.getPublicDirectory());
            ViewHelper.showToast(getContext(), errorMessage);
            return;
        }

        List<String> dateList = new ArrayList<>();
        for(File file : backupFiles) {
            try {
                DateTime date = Export.getBackupDate(file);
                if (date != null) {
                    String dateString = String.format("%s %s",
                            Helper.getDateFormat().print(date),
                            Helper.getTimeFormat().print(date));
                    dateList.add(dateString);
                }
            } catch (IllegalArgumentException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        String[] csvArrayDates = dateList.toArray(new String[dateList.size()]);
        builder.setTitle(R.string.backup_title)
                .setItems(csvArrayDates, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        importBackup(backupFiles.get(which));
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
    }

    private void importBackup(File file) {
        showProgressDialog();
        Export.importCsv(this, file);
    }

    private void showProgressDialog() {
        progressDialog.setMessage(getContext().getString(R.string.backup_import));
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
        Toast.makeText(getContext(), getContext().getString(R.string.backup_complete), Toast.LENGTH_SHORT).show();
    }
}