package com.faltenreich.diaguard.feature.export;

public interface Backupable {
    String getKeyForBackup();
    String[] getValuesForBackup();
}
