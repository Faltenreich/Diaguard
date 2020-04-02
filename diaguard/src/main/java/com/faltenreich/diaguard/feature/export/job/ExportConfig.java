package com.faltenreich.diaguard.feature.export.job;

import android.content.Context;

import com.faltenreich.diaguard.shared.data.preference.PreferenceHelper;
import com.faltenreich.diaguard.shared.data.database.entity.Category;

import org.joda.time.DateTime;

import java.lang.ref.WeakReference;

public abstract class ExportConfig {

    private final WeakReference<Context> contextReference;
    private final ExportCallback callback;
    private final DateTime dateStart;
    private final DateTime dateEnd;
    private final Category[] categories;
    private final FileType format;

    public ExportConfig(
        Context context,
        ExportCallback callback,
        DateTime dateStart,
        DateTime dateEnd,
        Category[] categories,
        FileType format
    ) {
        this.contextReference = new WeakReference<>(context);
        this.callback = callback;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.categories = categories;
        this.format = format;
    }

    public Context getContext() {
        return contextReference.get();
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

    public Category[] getCategories() {
        return categories;
    }

    public boolean hasCategory(Category category) {
        for (Category other : categories) {
            if (category == other) {
                return true;
            }
        }
        return false;
    }

    public FileType getFormat() {
        return format;
    }

    public void persistInSharedPreferences() {
        PreferenceHelper.getInstance().setExportCategories(categories);
    }
}
