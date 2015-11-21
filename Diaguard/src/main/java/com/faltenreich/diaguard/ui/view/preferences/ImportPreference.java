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
public class ImportPreference extends DialogPreference implements IFileListener {

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
        List<File> csvFiles = getBackupFiles();
        if(csvFiles.size() <= 0) {
            String errorMessage = String.format("%s %s",
                    getContext().getString(R.string.error_no_backups),
                    FileUtils.getStorageDirectory());
            // TODO: ViewHelper.showSnackbar(getView(), errorMessage);
            return;
        }

        final File[] csvArray = csvFiles.toArray(new File[csvFiles.size()]);
        final String[] csvArrayDates = new String[csvArray.length];
        for(int position = 0; position < csvArray.length; position++) {
            String fileName = csvArray[position].getName();
            String dateString = csvArray[position].getName().substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf("."));
            DateTime date = DateTimeFormat.forPattern("yyyyMMddHHmmss").parseDateTime(dateString);
            csvArrayDates[position] = Helper.getDateFormat().print(date) + " " +
                    Helper.getTimeFormat().print(date);
        }

        builder.setTitle(R.string.backup_title)
                .setItems(csvArrayDates, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        importBackup(csvArray[which]);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
    }

    private List<File> getBackupFiles() {
        File path = FileUtils.getStorageDirectory();
        File[] files = path.listFiles();
        List<File> csvFiles = new ArrayList<>();
        for (File file : files) {
            String fileName = file.getName();
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if(extension.equals(Export.FileType.CSV.getExtension()))
                csvFiles.add(0, file);
        }
        return csvFiles;
    }

    private void importBackup(File file) {
        showProgressDialog();
        Export.importCsv(this, new File(FileUtils.getStorageDirectory() + file.getName()));
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
        // TODO: User feedback
    }
}