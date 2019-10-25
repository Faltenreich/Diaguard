package com.faltenreich.diaguard.export.pdf.print;

import com.faltenreich.diaguard.export.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.export.pdf.view.MeasurementTablePdfView;
import com.pdfjet.Point;

public class PdfTable implements PdfPrintable {

    private static final float PADDING_PARAGRAPH = 20;

    private MeasurementTablePdfView table;

    public PdfTable(PdfExportCache cache, float width) {
        table = new MeasurementTablePdfView(cache, width);
    }

    @Override
    public float getHeight() {
        return table.getHeight() + PADDING_PARAGRAPH;
    }

    @Override
    public void drawOn(PdfPage page, Point position) throws Exception {
        table.setLocation(position.getX(), position.getY());
        table.drawOn(page);
    }
}
