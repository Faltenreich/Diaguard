package com.faltenreich.diaguard.feature.export.job;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.feature.export.history.ExportHistoryListItem;
import com.faltenreich.diaguard.feature.export.job.csv.CsvExport;
import com.faltenreich.diaguard.feature.export.job.csv.CsvExportConfig;
import com.faltenreich.diaguard.feature.export.job.csv.CsvImport;
import com.faltenreich.diaguard.feature.export.job.pdf.PdfExport;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportConfig;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.file.FileUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Export {

    private static final String TAG = Export.class.getSimpleName();

    public static final String BACKUP_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String FILE_BACKUP_1_3_PREFIX = "diaguard_backup_";
    private static final String FILE_BACKUP_1_3_DATE_FORMAT = "yyyyMMddHHmmss";

    private static final String EXPORT_FILE_NAME_PREFIX = "Diaguard";
    private static final String EXPORT_DATE_FORMAT = "yyyy-MM-dd_HH-mm";
    public static final String EXPORT_SUBJECT_SEPARATOR = " - ";

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
        Category[] categories
    ) {
        exportCsv(context, callback, dateStart, dateEnd, categories, false);
    }

    private static void exportCsv(
        Context context,
        ExportCallback callback,
        DateTime dateStart,
        DateTime dateEnd,
        Category[] categories,
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

    public static void importCsv(Context context, Uri uri, ImportCallback callback) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                callback.onError();
                return;
            }
            CsvImport csvImport = new CsvImport(inputStream);
            csvImport.setCallback(callback);
            csvImport.execute();
        } catch (FileNotFoundException exception) {
            Log.e(TAG, exception.toString());
            callback.onError();
        }
    }

    public static File getExportFile(ExportConfig config) {
        DateTime createdAt = DateTime.now();
        String dateFormatted = DateTimeFormat.forPattern(EXPORT_DATE_FORMAT).print(createdAt);
        String fileName = String.format("%s%s%s_%s.%s",
            FileUtils.getPublicDirectory(config.getContext()),
            File.separator,
            EXPORT_FILE_NAME_PREFIX,
            dateFormatted,
            config.getFormat().extension
        );
        return new File(fileName);
    }

    public static File getBackupFile(ExportConfig config, FileType type) {
        String fileName = String.format("%s%s%s%s.%s",
            FileUtils.getPublicDirectory(config.getContext()),
            File.separator,
            FILE_BACKUP_1_3_PREFIX,
            DateTimeFormat.forPattern(FILE_BACKUP_1_3_DATE_FORMAT).print(DateTime.now()),
            type.extension);
        return new File(fileName);
    }

    public static DateTimeFormatter getDateTimeFormatterForSubject() {
        return Helper.getDateFormat();
    }

    @Nullable
    public static ExportHistoryListItem getExportItem(File file) {
        DateTime createdAt = FileUtils.getCreatedAt(file);
        if (createdAt == null) {
            return null;
        }
        return new ExportHistoryListItem(file, createdAt);
    }
}
