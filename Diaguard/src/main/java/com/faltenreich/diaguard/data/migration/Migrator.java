package com.faltenreich.diaguard.data.migration;

import android.content.Context;

import com.faltenreich.diaguard.util.SystemUtils;

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
        int versionCode = SystemUtils.getVersionCode(context);
        if (versionCode == 39) {
            new MigrateSodiumTask(context).execute();
        }
    }
}
