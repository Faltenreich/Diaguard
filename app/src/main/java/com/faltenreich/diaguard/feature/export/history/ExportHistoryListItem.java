package com.faltenreich.diaguard.feature.export.history;

import androidx.annotation.NonNull;

import org.joda.time.DateTime;

import java.io.File;

public class ExportHistoryListItem {

    @NonNull
    private File file;
    @NonNull
    private DateTime createdAt;

    public ExportHistoryListItem(
        @NonNull File file,
        @NonNull DateTime createdAt
    ) {
        this.file = file;
        this.createdAt = createdAt;
    }

    @NonNull
    public File getFile() {
        return file;
    }

    @NonNull
    public DateTime getCreatedAt() {
        return createdAt;
    }
}
