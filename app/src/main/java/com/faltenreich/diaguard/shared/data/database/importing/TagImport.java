package com.faltenreich.diaguard.shared.data.database.importing;

import android.content.Context;
import android.util.Log;

import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

class TagImport implements Importing {

    private static final String TAG = TagImport.class.getSimpleName();
    private static final String TAGS_CSV_FILE_NAME = "tags.csv";

    private final Context context;
    private final Locale locale;

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
        try {
            CSVReader reader = CsvImport.getCsvReader(context, TAGS_CSV_FILE_NAME);

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
            PreferenceStore.getInstance().setDidImportTags(locale, true);

        } catch (IOException exception) {
            Log.e(TAG, exception.toString());
        }
    }
}
