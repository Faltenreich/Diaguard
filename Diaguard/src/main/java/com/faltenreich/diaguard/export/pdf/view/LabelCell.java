package com.faltenreich.diaguard.export.pdf.view;

import com.faltenreich.diaguard.export.pdf.print.PdfTable;
import com.pdfjet.Cell;
import com.pdfjet.Color;
import com.pdfjet.Font;

public class LabelCell extends Cell {

    public LabelCell(Font font, String label, int backgroundColor) {
        super(font);
        setText(label);
        setBgColor(backgroundColor);
        setFgColor(Color.gray);
        setWidth(PdfTable.LABEL_WIDTH);
    }
}
