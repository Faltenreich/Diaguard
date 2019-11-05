package com.faltenreich.diaguard.export.pdf.view;

import com.pdfjet.Align;
import com.pdfjet.Cell;
import com.pdfjet.Color;
import com.pdfjet.Font;

public class HourCell extends Cell {

    public HourCell(Font font, int hour, float cellWidth) {
        super(font);
        setText(Integer.toString(hour));
        setWidth(cellWidth);
        setFgColor(Color.gray);
        setTextAlignment(Align.CENTER);
    }
}
