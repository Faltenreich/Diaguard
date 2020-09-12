package com.faltenreich.diaguard.shared.data.database.importing;

import android.content.Context;

import com.faltenreich.diaguard.shared.Helper;

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
        new TagImport(context, locale).importDataIfNeeded();
        new FoodImport(context, locale).importDataIfNeeded();
        new TestDataImport().importDataIfNeeded();
        new DemoDataImport(context).importDataIfNeeded();
    }
}
