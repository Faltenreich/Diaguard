package com.faltenreich.diaguard.shared.data.database.importing;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.faltenreich.diaguard.feature.export.job.Export;

import java.io.IOException;
import java.io.InputStream;

class DemoDataImport implements Importing {

    private static final String TAG = DemoDataImport.class.getSimpleName();
    private static final String BACKUP_FILE_NAME = "backup.csv";

    private Context context;

    DemoDataImport(Context context) {
        this.context = context;
    }

    @Override
    public boolean requiresImport() {
        // TODO: Determine screenshot task
        return false;
    }

    @Override
    public void importData() {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open(BACKUP_FILE_NAME);
            Export.importCsv(inputStream, null);
        } catch (IOException exception) {
            Log.e(TAG, exception.toString());
        }
    }
}
