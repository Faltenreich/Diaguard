package com.faltenreich.diaguard.ui.list.item;

import org.joda.time.DateTime;

import java.io.File;

public class ListItemExportHistory {

    private File file;
    private DateTime createdAt;

    public ListItemExportHistory(File file, DateTime createdAt) {
        this.file = file;
        this.createdAt = createdAt;
    }

    public File getFile() {
        return file;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }
}
