package com.faltenreich.diaguard.ui.list.item;

import java.io.File;

public class ListItemExportHistory {

    private File file;

    public ListItemExportHistory(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
