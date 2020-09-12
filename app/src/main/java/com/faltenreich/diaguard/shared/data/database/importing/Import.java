package com.faltenreich.diaguard.shared.data.database.importing;

import android.content.Context;
import android.os.AsyncTask;

import com.faltenreich.diaguard.shared.Helper;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class Import implements Importing {

    private Context context;
    private Locale locale;

    public Import(Context context) {
        this.context = context;
        this.locale = Helper.getLocale(context);
    }

    @Override
    public boolean requiresImport() {
        return true;
    }

    @Override
    public void importData() {
        new ImportTask(context, locale).execute();
    }

    private static class ImportTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<Context> contextReference;
        private Locale locale;

        private ImportTask(Context context, Locale locale) {
            this.contextReference = new WeakReference<>(context);
            this.locale = locale;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Context context = contextReference.get();
            new TagImport(context, locale).importDataIfNeeded();
            new FoodImport(context, locale).importDataIfNeeded();
            new TestDataImport().importDataIfNeeded();
            new DemoDataImport(context).importDataIfNeeded();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
