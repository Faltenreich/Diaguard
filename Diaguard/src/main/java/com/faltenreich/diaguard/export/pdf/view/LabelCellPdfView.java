package com.faltenreich.diaguard.export.pdf.view;

import com.faltenreich.diaguard.export.pdf.print.PdfTable;
import com.pdfjet.Cell;
import com.pdfjet.Color;
import com.pdfjet.Font;

public class LabelCellPdfView extends Cell {

    public LabelCellPdfView(Font font, String label, int backgroundColor) {
        super(font, label);
        setBgColor(backgroundColor);
        setFgColor(Color.gray);
        setWidth(PdfTable.LABEL_WIDTH);
        setNoBorders();
    }
}
