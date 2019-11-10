package com.faltenreich.diaguard.export.pdf.print;

import com.faltenreich.diaguard.export.pdf.meta.PdfExportCache;

public interface PdfPageable extends PdfPrintable {

    default void onNewPage(PdfExportCache cache, PdfPage page) {
        page.draw(new PdfWeek(cache));
    }
}
