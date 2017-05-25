package com.faltenreich.diaguard.ui.view.preferences;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.PermissionDeniedEvent;
import com.faltenreich.diaguard.event.PermissionGrantedEvent;
import com.faltenreich.diaguard.util.FileUtils;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.SystemUtils;
import com.faltenreich.diaguard.util.ViewHelper;
import com.faltenreich.diaguard.util.export.Export;
import com.faltenreich.diaguard.util.export.FileListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 04.11.13.
 */
public class ImportPreference extends Preference implements Preference.OnPreferenceClickListener, FileListener {

    private static final String TAG = ImportPreference.class.getSimpleName();

    private ProgressDialog progressDialog;

    public ImportPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        tryListBackups();
        return true;
    }

    private void tryListBackups() {
        Activity activity = (Activity) getContext();
        if (SystemUtils.canWriteExternalStorage(activity)) {
            listBackups();
        } else {
            Events.register(this);
            SystemUtils.requestPermissionWriteExternalStorage(activity);
        }
    }

    private void listBackups() {
        final List<File> backupFiles = Export.getBackupFiles();
        if (backupFiles.size() <= 0) {
            String errorMessage = String.format("%s %s",
                    getContext().getString(R.string.error_no_backups),
                    FileUtils.getPublicDirectory());
            ViewHelper.showToast(getContext(), errorMessage);
            return;
        }

        List<String> dateList = new ArrayList<>();
        for (File file : backupFiles) {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.backup_title)
                .setItems(csvArrayDates, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        importBackup(backupFiles.get(which));
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void importBackup(File file) {
        showProgressDialog();
        Export.importCsv(this, file);
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PermissionGrantedEvent event) {
        if (event.context.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            listBackups();
            Events.unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PermissionDeniedEvent event) {
        if (event.context.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ViewHelper.showToast(getContext(), R.string.permission_required_storage);
            Events.unregister(this);
        }
    }
}