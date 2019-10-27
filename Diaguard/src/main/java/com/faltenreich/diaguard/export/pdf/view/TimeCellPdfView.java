package com.faltenreich.diaguard.export.pdf.view;

import com.faltenreich.diaguard.export.pdf.print.PdfTable;
import com.faltenreich.diaguard.util.Helper;
import com.pdfjet.Cell;
import com.pdfjet.Color;
import com.pdfjet.Font;

import org.joda.time.DateTime;

public class TimeCellPdfView extends Cell {

    public TimeCellPdfView(Font font, DateTime dateTime) {
        super(font);
        setText(Helper.getTimeFormat().print(dateTime));
        setFgColor(Color.gray);
        setWidth(PdfTable.LABEL_WIDTH);
        setNoBorders();
    }
}
