package com.faltenreich.diaguard.shared.data.file;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;

public class FileUtils {

    public static File getPublicDirectory(Context context) {
        File directory;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        } else if (Build.VERSION.SDK_INT >= 19) {
            directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        } else {
            directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        }
        directory.mkdirs();
        return directory;
    }
}
