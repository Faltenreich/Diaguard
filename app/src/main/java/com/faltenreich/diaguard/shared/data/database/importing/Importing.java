package com.faltenreich.diaguard.shared.data.database.importing;

interface Importing {

    boolean requiresImport();

    void importData();

    default void importDataIfNeeded() {
        if (requiresImport()) {
            importData();
        }
    }
}
