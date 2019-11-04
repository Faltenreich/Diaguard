package com.faltenreich.diaguard.export.pdf.view;

import com.faltenreich.diaguard.export.pdf.print.PdfTable;
import com.pdfjet.Color;
import com.pdfjet.Font;

public class LabelCellPdfView extends PdfCellView {

    public LabelCellPdfView(Font font, String label, int backgroundColor) {
        super(font);
        setText(label);
        setBgColor(backgroundColor);
        setFgColor(Color.gray);
        setWidth(PdfTable.LABEL_WIDTH);
    }
}
