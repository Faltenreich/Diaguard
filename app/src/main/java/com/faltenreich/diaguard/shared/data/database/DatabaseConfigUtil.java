package com.faltenreich.diaguard.shared.data.database;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {

    private static final String currDirectory = "user.dir";
    private static final String configPath = "/app/src/main/res/raw/ormlite_config.txt";
    private static final String projectRoot = System.getProperty(currDirectory);
    private static final String fullConfigPath = projectRoot + configPath;

    public static void main(String[] args) throws IOException, SQLException {
        File configFile = new File(fullConfigPath);
        if (configFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            configFile.delete();
            configFile = new File(fullConfigPath);
        }
        writeConfigFile(configFile, DatabaseHelper.tables);
    }
}