package com.faltenreich.diaguard.export;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.export.csv.CsvExport;
import com.faltenreich.diaguard.export.csv.CsvExportConfig;
import com.faltenreich.diaguard.export.csv.CsvImport;
import com.faltenreich.diaguard.export.pdf.PdfExport;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportConfig;
import com.faltenreich.diaguard.util.FileUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.File;

public class Export {

    private static final String TAG = Export.class.getSimpleName();

    public static final String BACKUP_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String EXPORT_DATE_FORMAT_4_0_0 = "yyyy-MM-dd_HH-mm-ss";
    private static final String EXPORT_DATE_FORMAT_3_3_0 = "yyyy-MM-dd_HH-mm";

    private static final String FILE_BACKUP_1_3_PREFIX = "diaguard_backup_";
    private static final String FILE_BACKUP_1_3_DATE_FORMAT = "yyyyMMddHHmmss";

    public static void exportPdf(PdfExportConfig config) {
        PdfExport pdfExport = new PdfExport(config);
        pdfExport.execute();
    }

    public static void exportCsv(Context context, ExportCallback callback) {
        exportCsv(context, callback, null, null, null, true);
    }

    public static void exportCsv(
        Context context,
        ExportCallback callback,
        DateTime dateStart,
        DateTime dateEnd,
        Measurement.Category[] categories
    ) {
        exportCsv(context, callback, dateStart, dateEnd, categories, false);
    }

    private static void exportCsv(
        Context context,
        ExportCallback callback,
        DateTime dateStart,
        DateTime dateEnd,
        Measurement.Category[] categories,
        boolean isBackup
    ) {
        CsvExportConfig config = new CsvExportConfig(
            context,
            callback,
            dateStart,
            dateEnd,
            categories,
            isBackup
        );
        if (!isBackup) {
            config.persistInSharedPreferences();
        }
        CsvExport csvExport = new CsvExport(config);
        csvExport.execute();
    }

    public static void importCsv(Context context, Uri uri, ExportCallback callback) {
        CsvImport csvImport = new CsvImport(context, uri);
        csvImport.setCallback(callback);
        csvImport.execute();
    }

    public static File getExportFile(Context context, ExportFormat type) {
        String fileName = String.format("%s%s%s_%s.%s",
            FileUtils.getPublicDirectory(),
            File.separator,
            context.getString(R.string.app_name),
            DateTimeFormat.forPattern(EXPORT_DATE_FORMAT_4_0_0).print(DateTime.now()),
            type.getExtension());
        return new File(fileName);
    }

    @Nullable
    public static DateTime getExportDateTime(Context context, File file) {
        String fileName = file.getName();
        String identifier = context.getString(R.string.app_name);
        if (fileName.startsWith(identifier)) {
            int startIndex = fileName.indexOf("_");
            int endIndex = fileName.indexOf(".");
            String dateTimeAsString = fileName.substring(startIndex + 1, endIndex);
            try {
                return DateTimeFormat.forPattern(EXPORT_DATE_FORMAT_4_0_0).parseDateTime(dateTimeAsString);
            } catch (IllegalArgumentException exception1) {
                try {
                    return DateTimeFormat.forPattern(EXPORT_DATE_FORMAT_3_3_0).parseDateTime(dateTimeAsString);
                } catch (IllegalArgumentException exception2) {
                    Log.e(TAG, exception2.getMessage());
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    public static File getBackupFile(ExportFormat type) {
        String fileName = String.format("%s%s%s%s.%s",
            FileUtils.getPublicDirectory(),
            File.separator,
            FILE_BACKUP_1_3_PREFIX,
            DateTimeFormat.forPattern(FILE_BACKUP_1_3_DATE_FORMAT).print(DateTime.now()),
            type.getExtension());
        return new File(fileName);
    }
}
