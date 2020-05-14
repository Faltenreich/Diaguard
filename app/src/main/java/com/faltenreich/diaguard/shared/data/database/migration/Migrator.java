package com.faltenreich.diaguard.shared.data.database.migration;

import android.content.Context;

import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;

public class Migrator {

    private static Migrator instance;

    public static Migrator getInstance() {
        if (instance == null) {
            instance = new Migrator();
        }
        return instance;
    }

    private Migrator() {}

    public void start(Context context) {
        int oldVersionCode = PreferenceStore.getInstance().getVersionCode();
        if (oldVersionCode == 39) {
            new MigrateSodiumTask(context).execute();
        }
    }
}
