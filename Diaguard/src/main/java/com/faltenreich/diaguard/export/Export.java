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
import com.faltenreich.diaguard.util.DateTimeUtils;
import com.faltenreich.diaguard.util.FileUtils;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;

import java.io.File;

public class Export {

    public static final String BACKUP_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String FILE_BACKUP_1_3_PREFIX = "diaguard_backup_";
    private static final String FILE_BACKUP_1_3_DATE_FORMAT = "yyyyMMddHHmmss";

    private static final String EXPORT_FILE_NAME_PREFIX = "Diaguard";
    private static final String EXPORT_DATE_FORMAT_3_3_0 = "yyyy-MM-dd_HH-mm";
    private static final String EXPORT_DATE_FORMAT_3_3_0_REGEX = "\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}";
    private static final String EXPORT_DATE_FORMAT_4_0_0 = "%d-%d-%d"; // millis: createdAt_start_end
    private static final String EXPORT_DATE_FORMAT_4_0_0_REGEX = "\\d+-\\d+-\\d+";


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
        DateTime startedAt = config.getDateStart();
        DateTime endedAt = config.getDateEnd();
        String dateFormatted = String.format(EXPORT_DATE_FORMAT_4_0_0,
            createdAt.getMillis(),
            startedAt.getMillis(),
            endedAt.getMillis()
        );
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

    @Nullable
    public static ListItemExportHistory getExportItem(File file) {
        String fileName = file.getName();
        // Strip prefix
        int startIndex = fileName.indexOf("_");
        if (startIndex == -1) {
            return null;
        }
        // Strip extension
        int endIndex = fileName.indexOf(".");
        if (endIndex == -1) {
            return null;
        }

        String substring = fileName.substring(startIndex + 1, endIndex);
        DateTime createdAt = FileUtils.getCreatedAt(file);

        if (substring.matches(EXPORT_DATE_FORMAT_4_0_0_REGEX)) {
            String[] datesFormatted = substring.split("-");
            DateTime startedAt = DateTimeUtils.parseFromMillis(datesFormatted[1]);
            DateTime endedAt = DateTimeUtils.parseFromMillis(datesFormatted[2]);
            Interval interval = null;
            if (startedAt != null && endedAt != null) {
                interval = new Interval(startedAt, endedAt);
            }
            return new ListItemExportHistory(file, createdAt, interval);
        } else if (substring.matches(EXPORT_DATE_FORMAT_3_3_0_REGEX)) {
            // Interval has not been available yet
            return new ListItemExportHistory(file, createdAt, null);
        } else {
            return null;
        }
    }
}
