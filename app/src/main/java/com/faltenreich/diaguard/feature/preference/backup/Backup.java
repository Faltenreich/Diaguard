package com.faltenreich.diaguard.feature.preference.backup;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.export.job.Export;
import com.faltenreich.diaguard.feature.export.job.ExportCallback;
import com.faltenreich.diaguard.feature.export.job.ImportCallback;
import com.faltenreich.diaguard.feature.export.job.csv.CsvExportConfig;
import com.faltenreich.diaguard.shared.data.file.FileUtils;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.file.BackupImportedEvent;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.progress.ProgressComponent;

import java.io.File;
import java.lang.ref.WeakReference;

public class Backup {

    private final ProgressComponent progressComponent = new ProgressComponent();

    public void exportBackup(Context context) {
        progressComponent.show(context);

        WeakReference<Context> contextReference = new WeakReference<>(context);
        ExportCallback callback = new ExportCallback() {

            @Override
            public void onProgress(@NonNull String message) {
                progressComponent.setMessage(message);
            }

            @Override
            public void onSuccess(@NonNull File file, @NonNull String mimeType) {
                progressComponent.dismiss();
                FileUtils.shareFile(contextReference.get(), file, R.string.backup_store);
            }
            @Override
            public void onError(@NonNull String message) {
                progressComponent.dismiss();
                ViewUtils.showToast(contextReference.get(), message);
            }
        };
        Export.exportCsv(new CsvExportConfig(context, callback));
    }

    public void searchBackups(Activity activity) {
        String mimeType = "text/*"; // Workaround: text/csv does not work for all apps
        FileUtils.searchFiles(activity, mimeType, BackupImportPreference.REQUEST_CODE_BACKUP_IMPORT);
    }

    public void importBackup(Context context, Uri uri) {
        progressComponent.show(context);
        WeakReference<Context> contextReference = new WeakReference<>(context);
        Export.importCsv(context, uri, new ImportCallback() {

            @Override
            public void onSuccess(String mimeType) {
                progressComponent.dismiss();
                ViewUtils.showToast(
                    contextReference.get(),
                    contextReference.get().getString(R.string.backup_complete)
                );
                Events.post(new BackupImportedEvent());
            }

            @Override
            public void onError() {
                progressComponent.dismiss();
                ViewUtils.showToast(
                    contextReference.get(),
                    contextReference.get().getString(R.string.error_import)
                );
            }
        });
    }
}
