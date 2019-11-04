package com.faltenreich.diaguard.export.pdf.view;

import com.pdfjet.Cell;
import com.pdfjet.Font;

public class PdfCellView extends Cell {

    public PdfCellView(Font font) {
        super(font);
        setNoBorders();
    }
}
