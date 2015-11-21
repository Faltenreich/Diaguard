package com.faltenreich.diaguard.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Filip on 05.06.2014.
 */
public class FileUtils {

    public static final String PATH_EXTERNAL = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String PATH_EXTERNAL_PARENT = Environment.getExternalStorageDirectory().getParent();
    public static final String PATH_STORAGE = File.separator + "Diaguard";

    public static File getPrivateDirectory() {
        File directory = new File(PATH_EXTERNAL);
        if (!directory.exists()) {
            directory = new File(PATH_EXTERNAL_PARENT);
        }
        directory = new File(directory.getAbsolutePath() + PATH_STORAGE);
        directory.mkdirs();
        return directory;
    }

    public static File getPublicDirectory() {
        String path = android.os.Build.VERSION.SDK_INT >= 19 ?
                Environment.DIRECTORY_DOCUMENTS :
                Environment.DIRECTORY_DOWNLOADS;
        File directory = Environment.getExternalStoragePublicDirectory(path);
        directory.mkdirs();
        return directory;
    }
}
