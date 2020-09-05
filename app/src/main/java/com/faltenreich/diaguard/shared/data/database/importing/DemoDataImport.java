package com.faltenreich.diaguard.shared.data.database.importing;

import android.os.AsyncTask;

class DemoDataImport implements Importing {

    private static final String TAG = DemoDataImport.class.getSimpleName();

    @Override
    public boolean requiresImport() {
        return false;
    }

    @Override
    public void importData() {
        new ImportDemoDataTask().execute();
    }

    private static class ImportDemoDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // TODO
            return null;
        }
    }
}
