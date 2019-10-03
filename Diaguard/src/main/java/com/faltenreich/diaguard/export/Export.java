package com.faltenreich.diaguard.export;

import android.content.Context;
import android.net.Uri;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.util.FileUtils;
import com.faltenreich.diaguard.export.csv.CsvExport;
import com.faltenreich.diaguard.export.csv.CsvImport;
import com.faltenreich.diaguard.export.pdf.PdfExport;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.File;

public class Export {

    public static final String BACKUP_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String FILE_BACKUP_1_3_PREFIX = "diaguard_backup_";
    private static final String FILE_BACKUP_1_3_DATE_FORMAT = "yyyyMMddHHmmss";

    public enum FileType {
        CSV,
        PDF;

        public String getExtension() {
            switch (this) {
                case CSV:
                    return "csv";
                case PDF:
                    return "pdf";
                default:
                    return null;
            }
        }
    }

    public static final String PDF_MIME_TYPE = "application/pdf";

    public static final String CSV_MIME_TYPE = "text/csv";
    public static final String CSV_IMPORT_MIME_TYPE = "text/*"; // Workaround: text/csv does not work for all apps
    public static final char CSV_DELIMITER = ';';
    public static final String CSV_KEY_META = "meta";

    public static void exportPdf(ExportConfig config, ExportCallback listener) {
        PdfExport pdfExport = new PdfExport(config);
        pdfExport.setCallback(listener);
        pdfExport.execute();
    }

    public static void exportCsv(ExportConfig config, boolean isBackup, ExportCallback listener) {
        CsvExport csvExport = new CsvExport(config, isBackup);
        csvExport.setCallback(listener);
        csvExport.execute();
    }

    public static void importCsv(Context context, Uri uri, ExportCallback listener) {
        CsvImport csvImport = new CsvImport(context, uri);
        csvImport.setCallback(listener);
        csvImport.execute();
    }

    public static File getExportFile(FileType fileType) {
        String fileName = String.format("%s%s%s_%s.%s",
            FileUtils.getPublicDirectory(),
            File.separator,
            DiaguardApplication.getContext().getString(R.string.app_name),
            DateTimeFormat.forPattern("yyyy-MM-dd_HH-mm").print(DateTime.now()),
            fileType.getExtension());
        return new File(fileName);
    }

    public static File getBackupFile(FileType fileType) {
        String fileName = String.format("%s%s%s%s.%s",
            FileUtils.getPublicDirectory(),
            File.separator,
            FILE_BACKUP_1_3_PREFIX,
            DateTimeFormat.forPattern(FILE_BACKUP_1_3_DATE_FORMAT).print(DateTime.now()),
            fileType.getExtension());
        return new File(fileName);
    }
}
