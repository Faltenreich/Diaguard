package com.faltenreich.diaguard.export.pdf.view;

import com.pdfjet.Align;
import com.pdfjet.Color;
import com.pdfjet.Font;

public class HourCellPdfView extends PdfCellView {

    public HourCellPdfView(Font font, int hour, float cellWidth) {
        super(font);
        setText(Integer.toString(hour));
        setWidth(cellWidth);
        setFgColor(Color.gray);
        setTextAlignment(Align.CENTER);
    }
}
