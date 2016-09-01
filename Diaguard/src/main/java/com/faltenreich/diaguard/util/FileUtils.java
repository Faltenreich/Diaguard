package com.faltenreich.diaguard.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.util.List;

/**
 * Created by Filip on 05.06.2014.
 */
public class FileUtils {

    public static final String PATH_EXTERNAL = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String PATH_EXTERNAL_PARENT = Environment.getExternalStorageDirectory().getParent();
    public static final String PATH_STORAGE = File.separator + "Diaguard";

    public static final char POINT = '.';

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

    public static void openFile(File file, String mimeType, Context context) throws ActivityNotFoundException {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        intent.setDataAndType(uri, mimeType);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        grantUriPermission(uri, intent, context);
        context.startActivity(intent);
        // TODO: revokeUriPermission onResult
    }

    private static List<ResolveInfo> getSupportingApps(Intent intent, Context context) {
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
    }

    private static void grantUriPermission(Uri uri, Intent intent, Context context) {
        for (ResolveInfo resolveInfo : getSupportingApps(intent, context)) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    private static void revokeUriPermission(Uri uri, Context context) {
        context.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }
}
