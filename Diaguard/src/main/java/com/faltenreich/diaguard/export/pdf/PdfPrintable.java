package com.faltenreich.diaguard.export.pdf;

import com.pdfjet.Point;

public interface PdfPrintable {

    Point drawOn(PdfPage page) throws Exception;
}
