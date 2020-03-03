package com.faltenreich.diaguard.data.migration;

import android.content.Context;

import com.faltenreich.diaguard.data.PreferenceHelper;

public class Migrator {

    private static Migrator instance;

    public static Migrator getInstance() {
        if (instance == null) {
            instance = new Migrator();
        }
        return instance;
    }

    private Migrator() {

    }

    public void start(Context context) {
        int oldVersionCode = PreferenceHelper.getInstance().getVersionCode();
        if (oldVersionCode == 39) {
            new MigrateSodiumTask(context).execute();
        }
    }
}
