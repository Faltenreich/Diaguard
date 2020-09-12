package com.faltenreich.diaguard.shared.data.database.importing;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.faltenreich.diaguard.BuildConfig;
import com.faltenreich.diaguard.feature.export.job.Export;
import com.faltenreich.diaguard.shared.Helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

class DemoDataImport implements Importing {

    private static final String TAG = DemoDataImport.class.getSimpleName();

    private Context context;

    DemoDataImport(Context context) {
        this.context = context;
    }

    @Override
    public boolean requiresImport() {
        return BuildConfig.FLAVOR.equals("demo");
    }

    @Override
    public void importData() {
        AssetManager assetManager = context.getAssets();
        try {
            Locale locale = Helper.getLocale(context);
            String localeIdentifier = locale.getLanguage();
            String fileName = String.format("backup/%s.csv", localeIdentifier);
            InputStream inputStream = assetManager.open(fileName);
            Export.importCsv(inputStream, null);
        } catch (IOException exception) {
            Log.e(TAG, exception.toString());
        }
    }
}
