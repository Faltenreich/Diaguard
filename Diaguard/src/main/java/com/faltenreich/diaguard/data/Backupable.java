package com.faltenreich.diaguard.data;

public interface Backupable {
    String getKeyForBackup();
    String[] getValuesForBackup();
}
