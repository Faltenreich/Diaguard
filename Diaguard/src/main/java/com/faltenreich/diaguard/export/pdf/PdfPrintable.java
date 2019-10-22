package com.faltenreich.diaguard.export.pdf;

public interface PdfPrintable {

    void drawOn(PdfPage page) throws Exception;
}
