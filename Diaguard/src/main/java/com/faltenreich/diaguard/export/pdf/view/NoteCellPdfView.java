package com.faltenreich.diaguard.export.pdf.view;

import com.pdfjet.Color;
import com.pdfjet.Font;

public class NoteCellPdfView extends MultilineCellPdfView {

    private static final int CHARACTER_COUNT = 55;

    public NoteCellPdfView(Font font, String text, float width) {
        super(font, text, CHARACTER_COUNT);
        setFgColor(Color.gray);
        setWidth(width);
        setNoBorders();
    }
}
