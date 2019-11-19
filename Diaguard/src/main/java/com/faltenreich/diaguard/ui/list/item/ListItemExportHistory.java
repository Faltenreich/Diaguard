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
    private Interval interval;
    @Nullable
    private DateTime createdAt;

    public ListItemExportHistory(
        @NonNull File file,
        @Nullable Interval interval,
        @Nullable DateTime createdAt
    ) {
        this.file = file;
        this.interval = interval;
        this.createdAt = createdAt;
    }

    @NonNull
    public File getFile() {
        return file;
    }

    @Nullable
    public Interval getInterval() {
        return interval;
    }

    @Nullable
    public DateTime getCreatedAt() {
        return createdAt;
    }
}
