package com.faltenreich.diaguard.feature.export.job.pdf.print;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportCache;
import com.pdfjet.Color;
import com.pdfjet.Point;
import com.pdfjet.TextLine;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class PdfFooter implements PdfPrintable {

    private TextLine createdBy;
    private TextLine url;

    public PdfFooter(PdfExportCache cache) {
        Context context = cache.getContext();

        createdBy = new TextLine(cache.getFontNormal());
        createdBy.setColor(Color.gray);
        createdBy.setText(String.format("%s %s",
            context.getString(R.string.export_stamp),
            DateTimeFormat.mediumDate().print(DateTime.now())));

        url = new TextLine(cache.getFontNormal());
        url.setColor(Color.gray);
        url.setText(context.getString(R.string.app_homepage_short));
    }

    @Override
    public float getHeight() {
        return createdBy.getHeight();
    }

    @Override
    public void drawOn(PdfPage page, Point position) throws Exception {
        createdBy.setPosition(position.getX(), position.getY());
        createdBy.drawOn(page);
        url.setPosition(position.getX() + page.getWidth() - url.getWidth(), position.getY());
        url.drawOn(page);
    }
}
