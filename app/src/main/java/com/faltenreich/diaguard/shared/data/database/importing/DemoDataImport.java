package com.faltenreich.diaguard.shared.data.database.importing;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;

import com.faltenreich.diaguard.feature.export.job.Export;

import java.lang.ref.WeakReference;

class DemoDataImport implements Importing {

    private Context context;

    DemoDataImport(Context context) {
        this.context = context;
    }

    @Override
    public boolean requiresImport() {
        return false;
    }

    @Override
    public void importData() {
        new ImportDemoDataTask(context).execute();
    }

    private static class ImportDemoDataTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<Context> contextReference;

        ImportDemoDataTask(Context context) {
            this.contextReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Context context = contextReference.get();
            AssetManager assetManager = context.getAssets();
            // TODO: How to get uri from asset?
            Uri uri = null;
            Export.importCsv(context, uri, null);
            return null;
        }
    }
}
