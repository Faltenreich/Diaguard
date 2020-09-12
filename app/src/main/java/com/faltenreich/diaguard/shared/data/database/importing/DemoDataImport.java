package com.faltenreich.diaguard.shared.data.database.importing;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.faltenreich.diaguard.BuildConfig;
import com.faltenreich.diaguard.feature.export.job.csv.CsvImport;
import com.faltenreich.diaguard.feature.export.job.date.DemoDateStrategy;
import com.faltenreich.diaguard.shared.Helper;

import org.joda.time.DateTime;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

class DemoDataImport implements Importing {

    private static final String TAG = DemoDataImport.class.getSimpleName();
    private static final DateTime maxDate = new DateTime().withDate(2020, 1, 31);

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

            CsvImport csvImport = new CsvImport(inputStream);
            csvImport.setDateStrategy(new DemoDateStrategy(maxDate));
            csvImport.execute();

        } catch (IOException exception) {
            Log.e(TAG, exception.toString());
        }
    }
}
