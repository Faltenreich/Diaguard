package com.faltenreich.diaguard.export.pdf.print;

import com.pdfjet.Point;

interface PdfPrintable {
    Point drawOn(PdfPage page) throws Exception;
}
