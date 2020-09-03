package com.faltenreich.diaguard.shared.data.database.importing;

import android.content.Context;

import com.faltenreich.diaguard.shared.Helper;

import java.util.Locale;

public class Import implements Importing {

    private Context context;

    public Import(Context context) {
        this.context = context;
    }

    @Override
    public void validateImport() {
        Locale locale = Helper.getLocale();
        new TagImport(context, locale).validateImport();
        new FoodImport(context, locale).validateImport();
        new TestDataImport().validateImport();
    }
}
