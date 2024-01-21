package com.faltenreich.diaguard.feature.export.job.csv;

import android.content.Context;

import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.feature.export.job.ExportCallback;
import com.faltenreich.diaguard.feature.export.job.ExportConfig;
import com.faltenreich.diaguard.feature.export.job.FileType;

import org.joda.time.DateTime;

public class CsvExportConfig extends ExportConfig {

    private final boolean isBackup;
    private final boolean exportNotes;
    private final boolean exportTags;

    public CsvExportConfig(
        Context context,
        ExportCallback callback,
        DateTime dateStart,
        DateTime dateEnd,
        Category[] categories,
        boolean isBackup,
        boolean exportNotes,
        boolean exportTags
    ) {
        super(context, callback, dateStart, dateEnd, categories, FileType.CSV);
        this.isBackup = isBackup;
        this.exportNotes = exportNotes;
        this.exportTags = exportTags;
    }

    public CsvExportConfig(
        Context context,
        ExportCallback callback
    ) {
        this(
            context,
            callback,
            null,
            null,
            null,
            true,
            true,
            true
        );
    }

    public boolean isBackup() {
        return isBackup;
    }

    public boolean exportNotes() {
        return exportNotes;
    }

    public boolean exportTags() {
        return exportTags;
    }

    @Override
    public void persistInSharedPreferences() {
        super.persistInSharedPreferences();
        PreferenceStore.getInstance().setExportNotes(exportNotes);
        PreferenceStore.getInstance().setExportTags(exportTags);
    }
}
