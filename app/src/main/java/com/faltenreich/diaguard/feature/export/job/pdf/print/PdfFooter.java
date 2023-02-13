package com.faltenreich.diaguard.feature.export.job.pdf.print;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportConfig;
import com.pdfjet.Color;
import com.pdfjet.Point;
import com.pdfjet.TextLine;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class PdfFooter {

    private final PdfExportCache cache;

    private final TextLine createdBy;
    private final TextLine pageCount;

    public PdfFooter(PdfExportCache cache) {
        this.cache = cache;

        createdBy = new TextLine(cache.getFontNormal());
        createdBy.setColor(Color.gray);
        createdBy.setText(String.format("%s %s",
            cache.getContext().getString(R.string.export_stamp),
            DateTimeFormat.mediumDate().print(DateTime.now()))
        );

        pageCount = new TextLine(cache.getFontNormal());
        pageCount.setColor(Color.gray);
    }

    public float getHeight() {
        PdfExportConfig config = cache.getConfig();
        boolean hasFooter = config.includeGeneratedDate() || config.includePageNumber();
        return hasFooter ? createdBy.getHeight() : 0;
    }

    public void drawOn(PdfPage page, Point position) throws Exception {
        PdfExportConfig config = cache.getConfig();

        if (config.includeGeneratedDate()) {
            createdBy.setPosition(position.getX(), position.getY());
            createdBy.drawOn(page);
        }

        if (config.includePageNumber()) {
            pageCount.setText(cache.getContext().getString(R.string.export_page, cache.getPageCount()));
            pageCount.setPosition(position.getX() + page.getWidth() - pageCount.getWidth(), position.getY());
            pageCount.drawOn(page);
        }
    }
}
