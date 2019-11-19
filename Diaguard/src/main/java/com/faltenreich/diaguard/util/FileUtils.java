package com.faltenreich.diaguard.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.faltenreich.diaguard.R;

import java.io.File;
import java.util.List;

public class FileUtils {

    public static File getPublicDirectory() {
        String path = android.os.Build.VERSION.SDK_INT >= 19 ?
                Environment.DIRECTORY_DOCUMENTS :
                Environment.DIRECTORY_DOWNLOADS;
        File directory = Environment.getExternalStoragePublicDirectory(path);
        directory.mkdirs();
        return directory;
    }

    private static Uri getUriForFile(Context context, File file) {
        return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
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

    public static void openFile(File file, String mimeType, Context context) throws ActivityNotFoundException {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = getUriForFile(context, file);
        intent.setDataAndType(uri, mimeType);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        grantUriPermission(uri, intent, context);
        context.startActivity(intent);
    }

    public static void shareFile(@NonNull Context context, @NonNull File file, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_STREAM, getUriForFile(context, file));
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.backup_store)));
    }

    public static void searchFiles(Activity activity, String mimeType, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(mimeType);
        activity.startActivityForResult(intent, requestCode);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            String[] children = directory.list();
            for (String child : children) {
                deleteDirectory(new File(directory, child));
            }
        }
        directory.delete();
    }
}
