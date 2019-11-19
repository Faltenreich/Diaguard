package com.faltenreich.diaguard.ui.list.item;

import org.joda.time.DateTime;

import java.io.File;

public class ListItemExportHistory {

    private File file;
    private DateTime dateTime;

    public ListItemExportHistory(File file, DateTime dateTime) {
        this.file = file;
        this.dateTime = dateTime;
    }

    public File getFile() {
        return file;
    }

    public DateTime getDateTime() {
        return dateTime;
    }
}
