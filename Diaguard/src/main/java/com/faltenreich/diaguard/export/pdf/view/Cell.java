package com.faltenreich.diaguard.export.pdf.view;

import com.pdfjet.Font;

public class Cell extends com.pdfjet.Cell {

    public Cell(Font font) {
        super(font);
        setNoBorders();
    }
}
