package com.faltenreich.diaguard.export;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;

import org.joda.time.DateTime;

public abstract class ExportConfig {

    private final ExportCallback callback;
    private final DateTime dateStart;
    private final DateTime dateEnd;
    private final Measurement.Category[] categories;

    public ExportConfig(
        ExportCallback callback,
        DateTime dateStart,
        DateTime dateEnd,
        Measurement.Category[] categories
    ) {
        this.callback = callback;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.categories = categories;
    }

    public ExportCallback getCallback() {
        return callback;
    }

    public DateTime getDateStart() {
        return dateStart;
    }

    public DateTime getDateEnd() {
        return dateEnd;
    }

    public Measurement.Category[] getCategories() {
        return categories;
    }

    public void persistInSharedPreferences() {
        PreferenceHelper.getInstance().setExportCategories(categories);
    }
}
