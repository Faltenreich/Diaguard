package com.faltenreich.diaguard.ui.list.item;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.io.File;

public class ListItemExportHistory {

    @NonNull
    private File file;
    @Nullable
    private DateTime createdAt;
    @Nullable
    private Interval interval;

    public ListItemExportHistory(
        @NonNull File file,
        @Nullable DateTime createdAt,
        @Nullable Interval interval
    ) {
        this.file = file;
        this.createdAt = createdAt;
        this.interval = interval;
    }

    @NonNull
    public File getFile() {
        return file;
    }

    @Nullable
    public DateTime getCreatedAt() {
        return createdAt;
    }

    @Nullable
    public Interval getInterval() {
        return interval;
    }
}
