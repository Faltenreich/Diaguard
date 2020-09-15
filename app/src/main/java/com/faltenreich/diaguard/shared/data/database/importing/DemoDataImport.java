package com.faltenreich.diaguard.shared.data.database.importing;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.faltenreich.diaguard.feature.export.job.csv.CsvImport;
import com.faltenreich.diaguard.feature.export.job.date.DemoDateStrategy;
import com.faltenreich.diaguard.feature.config.ApplicationConfig;

import org.joda.time.DateTime;

import java.io.IOException;
import java.io.InputStream;

class DemoDataImport implements Importing {

    private static final String TAG = DemoDataImport.class.getSimpleName();
    private static final String DEMO_BACKUP_FILE_NAME = "backup.csv";
    private static final DateTime MAXIMUM_DATE_IN_BACKUP = new DateTime().withDate(2020, 1, 31);

    private Context context;

    DemoDataImport(Context context) {
        this.context = context;
    }

    @Override
    public boolean requiresImport() {
        return ApplicationConfig.getFlavor().importDemoData();
    }

    @Override
    public void importData() {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(DEMO_BACKUP_FILE_NAME);

            CsvImport csvImport = new CsvImport(inputStream);
            csvImport.setDateStrategy(new DemoDateStrategy(MAXIMUM_DATE_IN_BACKUP));
            csvImport.execute();

        } catch (IOException exception) {
            Log.e(TAG, exception.toString());
        }
    }
}
