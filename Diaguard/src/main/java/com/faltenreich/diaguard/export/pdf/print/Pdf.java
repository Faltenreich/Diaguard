package com.faltenreich.diaguard.export.pdf.print;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.export.Export;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportConfig;
import com.pdfjet.PDF;

import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileOutputStream;

public class Pdf extends PDF {

    public Pdf(File file, PdfExportConfig config) throws Exception {
        super(new FileOutputStream(file));
        init(config);
    }

    private void init(PdfExportConfig config) {
        Context context = config.getContext();
        DateTimeFormatter formatter = Export.getDateTimeFormatterForSubject();
        setCreator(context.getString(R.string.app_name));
        setTitle(String.format("%s %s",
            context.getString(R.string.app_name),
            context.getString(R.string.export))
        );
        // Reminder: Subject started with a prefix pre-3.3.0: Diaguard Export:
        setSubject(String.format("%s%s%s",
            formatter.print(config.getDateStart()),
            Export.EXPORT_SUBJECT_SEPARATOR,
            formatter.print(config.getDateEnd()))
        );
    }
}
