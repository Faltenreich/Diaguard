package com.faltenreich.diaguard.export;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.export.csv.CsvExport;
import com.faltenreich.diaguard.export.csv.CsvExportConfig;
import com.faltenreich.diaguard.export.csv.CsvImport;
import com.faltenreich.diaguard.export.pdf.PdfExport;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportConfig;
import com.faltenreich.diaguard.ui.list.item.ListItemExportHistory;
import com.faltenreich.diaguard.util.FileUtils;
import com.faltenreich.diaguard.util.Helper;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;

public class Export {

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

    public static File getExportFile(ExportConfig config) {
        DateTime createdAt = DateTime.now();
        String dateFormatted = DateTimeFormat.forPattern(EXPORT_DATE_FORMAT).print(createdAt);
        String fileName = String.format("%s%s%s_%s.%s",
            FileUtils.getPublicDirectory(),
            File.separator,
            EXPORT_FILE_NAME_PREFIX,
            dateFormatted,
            config.getFormat().extension
        );
        return new File(fileName);
    }

    public static File getBackupFile(FileType type) {
        String fileName = String.format("%s%s%s%s.%s",
            FileUtils.getPublicDirectory(),
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
    public static ListItemExportHistory getExportItem(File file) {
        DateTime createdAt = FileUtils.getCreatedAt(file);
        if (createdAt == null) {
            return null;
        }
        return new ListItemExportHistory(file, createdAt);
    }
}
