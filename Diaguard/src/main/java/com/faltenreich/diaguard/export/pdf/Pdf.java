package com.faltenreich.diaguard.export.pdf;

import android.content.Context;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.util.Helper;
import com.pdfjet.PDF;

import java.io.File;
import java.io.FileOutputStream;

public class Pdf extends PDF {

    Pdf(File file, PdfExportConfig config) throws Exception {
        super(new FileOutputStream(file));
        init(config);
    }

    private void init(PdfExportConfig config) {
        Context context = config.getContextReference().get();
        setTitle(String.format("%s %s",
            context.getString(R.string.app_name),
            context.getString(R.string.export)));
        setSubject(String.format("%s %s: %s - %s",
            DiaguardApplication.getContext().getString(R.string.app_name),
            context.getString(R.string.export),
            Helper.getDateFormat().print(config.getDateStart()),
            Helper.getDateFormat().print(config.getDateEnd())));
        setAuthor(context.getString(R.string.app_name));
    }
}
