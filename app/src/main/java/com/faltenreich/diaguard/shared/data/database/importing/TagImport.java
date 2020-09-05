package com.faltenreich.diaguard.shared.data.database.importing;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

class TagImport implements Importing {

    private static final String TAG = TagImport.class.getSimpleName();
    private static final String TAGS_CSV_FILE_NAME = "tags.csv";

    private Context context;
    private Locale locale;

    TagImport(Context context, Locale locale) {
        this.context = context;
        this.locale = locale;
    }

    @Override
    public boolean requiresImport() {
        return !PreferenceStore.getInstance().didImportTags(locale);
    }

    @Override
    public void importData() {
        new ImportTagsTask(context, locale).execute();
    }

    private static class ImportTagsTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<Context> context;
        private Locale locale;

        ImportTagsTask(Context context, Locale locale) {
            this.context = new WeakReference<>(context);
            this.locale = locale;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                CSVReader reader = CsvImport.getCsvReader(context.get(), TAGS_CSV_FILE_NAME);

                String languageCode = locale.getLanguage();
                String[] nextLine = reader.readNext();
                int languageRow = CsvImport.getLanguageColumn(languageCode, nextLine);

                List<Tag> tags = new ArrayList<>();
                while ((nextLine = reader.readNext()) != null) {
                    if (nextLine.length >= 1) {
                        Tag tag = new Tag();
                        tag.setName(nextLine[languageRow]);
                        tags.add(tag);
                    }
                }

                TagDao.getInstance().deleteAll();
                Collections.reverse(tags);
                TagDao.getInstance().bulkCreateOrUpdate(tags);

                Log.i(TAG, String.format("Imported %d tags from csv", tags.size()));

            } catch (IOException exception) {
                Log.e(TAG, exception.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            PreferenceStore.getInstance().setDidImportTags(locale, true);
        }
    }
}
