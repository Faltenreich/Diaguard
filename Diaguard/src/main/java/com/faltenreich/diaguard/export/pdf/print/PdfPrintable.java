package com.faltenreich.diaguard.export.pdf.print;

import com.pdfjet.Point;

interface PdfPrintable {
    float getHeight();
    void drawOn(PdfPage page, Point position) throws Exception;
}
