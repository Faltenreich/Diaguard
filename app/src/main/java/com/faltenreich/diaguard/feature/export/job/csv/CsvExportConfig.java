package com.faltenreich.diaguard.feature.export.job.csv;

import android.content.Context;

import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.feature.export.job.ExportCallback;
import com.faltenreich.diaguard.feature.export.job.ExportConfig;
import com.faltenreich.diaguard.feature.export.job.FileType;

import org.joda.time.DateTime;

public class CsvExportConfig extends ExportConfig {

    private final boolean isBackup;

    public CsvExportConfig(
        Context context,
        ExportCallback callback,
        DateTime dateStart,
        DateTime dateEnd,
        Category[] categories,
        boolean isBackup
    ) {
        super(context, callback, dateStart, dateEnd, categories, FileType.CSV);
        this.isBackup = isBackup;
    }

    boolean isBackup() {
        return isBackup;
    }
}
