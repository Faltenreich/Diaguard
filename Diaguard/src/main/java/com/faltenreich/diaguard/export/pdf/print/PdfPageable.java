package com.faltenreich.diaguard.export.pdf.print;

public interface PdfPageable extends PdfPrintable {
    void onNewPage(PdfPage page);
}
