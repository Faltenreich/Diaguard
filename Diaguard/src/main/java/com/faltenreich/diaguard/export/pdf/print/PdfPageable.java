package com.faltenreich.diaguard.export.pdf.print;

import com.faltenreich.diaguard.export.pdf.meta.PdfExportCache;

public interface PdfPageable extends PdfPrintable {

    default void onNewPage(PdfExportCache cache, PdfPage page) {
        if (cache.getConfig().isExportHeader()) {
            page.draw(new PdfWeek(cache));
        }
    }
}
