package com.faltenreich.diaguard.export;

import android.content.Context;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;

import org.joda.time.DateTime;

import java.lang.ref.WeakReference;

public abstract class ExportConfig {

    private final WeakReference<Context> contextReference;
    private final ExportCallback callback;
    private final DateTime dateStart;
    private final DateTime dateEnd;
    private final Measurement.Category[] categories;

    public ExportConfig(
        Context context,
        ExportCallback callback,
        DateTime dateStart,
        DateTime dateEnd,
        Measurement.Category[] categories
    ) {
        this.contextReference = new WeakReference<>(context);
        this.callback = callback;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.categories = categories;
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

    public Measurement.Category[] getCategories() {
        return categories;
    }

    public void persistInSharedPreferences() {
        PreferenceHelper.getInstance().setExportCategories(categories);
    }
}
