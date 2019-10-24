package com.faltenreich.diaguard.export.pdf.print;

import com.faltenreich.diaguard.export.pdf.PdfExportCache;
import com.pdfjet.Point;

public class PdfTableBody implements PdfPrintable {

    private static final float PADDING_PARAGRAPH = 20;

    private PdfTable table;

    public PdfTableBody(PdfExportCache cache, float width) {
        table = new PdfTable(cache, width);
    }

    @Override
    public float getHeight() {
        return table.getHeight() + PADDING_PARAGRAPH;
    }

    @Override
    public void drawOn(PdfPage page, Point position) throws Exception {
        table.setPosition(position.getX(), position.getY());
        table.drawOn(page);
    }
}
