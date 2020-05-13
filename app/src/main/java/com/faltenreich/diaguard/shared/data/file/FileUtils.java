package com.faltenreich.diaguard.shared.data.file;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.FileProvider;

import com.faltenreich.diaguard.feature.export.job.FileType;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

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

    public static void openFile(Context context, File file) throws ActivityNotFoundException {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = getUriForFile(context, file);
        intent.setDataAndType(uri, FileType.mimeTypeOf(file));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        grantUriPermission(uri, intent, context);
        context.startActivity(intent);
    }

    public static void shareFile(@NonNull Context context, @NonNull File file, @StringRes int chooserTitleRes) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(FileType.mimeTypeOf(file));
        intent.putExtra(Intent.EXTRA_STREAM, getUriForFile(context, file));
        context.startActivity(Intent.createChooser(intent, context.getString(chooserTitleRes)));
    }

    public static void searchFiles(Activity activity, String mimeType, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(mimeType);
        activity.startActivityForResult(intent, requestCode);
    }

    public static boolean deleteFile(File file) {
        return file.delete();
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

    @Nullable
    public static DateTime getCreatedAt(File file) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                BasicFileAttributes attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                long creationTime = attributes.creationTime().toMillis();
                return creationTime > 0 ? new DateTime(creationTime) : null;
            } catch (IOException exception) {
                Log.e(TAG, exception.getMessage());
            }
        }
        // Fallback to last modified date
        long lastModified = file.lastModified();
        return lastModified > 0 ? new DateTime(lastModified) : null;
    }
}
