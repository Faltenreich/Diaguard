package com.faltenreich.diaguard.export.pdf.print;

import com.pdfjet.Point;

public interface PdfPrintable {

    float getHeight();

    default float getLabelWidth() {
        return 100;
    }

    void drawOn(PdfPage page, Point position) throws Exception;
}
