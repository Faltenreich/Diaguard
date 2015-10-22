package com.faltenreich.diaguard.util.export;

import android.support.annotation.NonNull;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.FileUtils;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.IFileListener;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.File;

/**
 * Created by Faltenreich on 21.10.2015.
 */
public class Export {

    public enum FileType {
        CSV,
        PDF;

        public String getMimeType() {
            switch (this) {
                case CSV:
                    return "text/csv";
                case PDF:
                    return "application/pdf";
                default:
                    return null;
            }
        }

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
    public static final char CSV_DELIMITER = ';';
    public static final String CSV_KEY_META = "meta";

    public static void exportPdf(IFileListener listener, @NonNull DateTime dateStart, @NonNull DateTime dateEnd, @NonNull Measurement.Category[] categories) {
        PdfExport pdfExport = new PdfExport(dateStart, dateEnd, categories);
        pdfExport.setListener(listener);
        pdfExport.execute();
    }

    public static void exportCsv(IFileListener listener, boolean isBackup, DateTime dateStart, DateTime dateEnd, Measurement.Category[] categories) {
        CsvExport csvExport = new CsvExport(isBackup, dateStart, dateEnd, categories);
        csvExport.setListener(listener);
        csvExport.execute();
    }

    public static void importCsv(@NonNull IFileListener listener, @NonNull File file) {
        CsvImport csvImport = new CsvImport(file);
        csvImport.setListener(listener);
        csvImport.execute();
    }

    public static DateTime dateTimeFromCsv(String dateString) {
        return Helper.getDateTimeFormatExport().parseDateTime(dateString);
    }

    public static File getExportFile(FileType fileType) {
        String fileName = String.format("%s%s%s_%s.%s",
                FileUtils.getStorageDirectory(),
                File.separator,
                DiaguardApplication.getContext().getString(R.string.app_name),
                DateTimeFormat.forPattern("yyyy-MM-dd_HH-mm").print(DateTime.now()),
                fileType.getExtension());
        return new File(fileName);
    }

    public static File getBackupFile(FileType fileType) {
        String fileName = String.format("%s%sbackup_%s.%s",
                FileUtils.getStorageDirectory(),
                File.separator,
                DateTimeFormat.forPattern("yyyyMMddHHmmss").print(DateTime.now()),
                fileType.getExtension());
        return new File(fileName);
    }
}
