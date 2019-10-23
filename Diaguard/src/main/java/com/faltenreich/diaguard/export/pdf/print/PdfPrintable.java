package com.faltenreich.diaguard.export.pdf.print;

import com.pdfjet.Point;

interface PdfPrintable {
    float getHeight();
    Point drawOn(PdfPage page) throws Exception;
}
