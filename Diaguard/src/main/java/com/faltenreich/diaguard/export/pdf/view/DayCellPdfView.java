package com.faltenreich.diaguard.export.pdf.view;

import com.faltenreich.diaguard.export.pdf.print.PdfTable;
import com.pdfjet.Cell;
import com.pdfjet.Font;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class DayCellPdfView extends Cell {

    public DayCellPdfView(Font font, DateTime day) {
        super(font);
        String weekDayString = DateTimeFormat.forPattern("E").print(day);
        // TODO: Localize dateString
        String dateString = DateTimeFormat.forPattern("dd.MM").print(day);
        String text = String.format("%s %s", weekDayString, dateString);
        setText(text);
        setWidth(PdfTable.LABEL_WIDTH);
        setNoBorders();
    }
}
