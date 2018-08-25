package com.faltenreich.diaguard.ui.view.preferences;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.widget.Toast;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.PermissionDeniedEvent;
import com.faltenreich.diaguard.event.PermissionGrantedEvent;
import com.faltenreich.diaguard.util.FileUtils;
import com.faltenreich.diaguard.util.SystemUtils;
import com.faltenreich.diaguard.util.ViewUtils;
import com.faltenreich.diaguard.util.export.Export;
import com.faltenreich.diaguard.util.export.FileListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

/**
 * Created by Filip on 04.11.13.
 */
public class BackupPreference extends Preference implements Preference.OnPreferenceClickListener, FileListener {

    private ProgressDialog progressDialog;

    public BackupPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        tryCreateBackup();
        return true;
    }

    private void tryCreateBackup() {
        Activity activity = (Activity) getContext();
        if (SystemUtils.canWriteExternalStorage(activity)) {
            createBackup();
        } else {
            Events.register(this);
            SystemUtils.requestPermissionWriteExternalStorage(activity);
        }
    }

    private void createBackup() {
        showProgressDialog();
        Export.exportCsv(this, true);
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.export_progress));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onProgress(String message) {
        if (progressDialog != null) {
            progressDialog.setMessage(message);
        }
    }

    @Override
    public void onComplete(File file, String mimeType) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (file != null) {
            FileUtils.shareFile(getContext(), file, mimeType);
        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PermissionGrantedEvent event) {
        if (event.context.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            createBackup();
            Events.unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PermissionDeniedEvent event) {
        if (event.context.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ViewUtils.showToast(getContext(), R.string.permission_required_storage);
            Events.unregister(this);
        }
    }
}