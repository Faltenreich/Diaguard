package com.faltenreich.diaguard.export.pdf.view;

import com.pdfjet.Border;
import com.pdfjet.Color;
import com.pdfjet.Font;
import com.pdfjet.Page;

public class NoteCellPdfView extends MultilineCellPdfView {

    public NoteCellPdfView(Font font, String text, float width) {
        super(font);
        setText(text);
        setFgColor(Color.gray);
        setWidth(width);
        setNoBorders();
    }

    @Override
    protected void paint(Page page, float x, float y, float w, float h) throws Exception {
        super.paint(page, x, y, w, h);
        drawBorders(page, x, y, w, h);
    }

    private void drawBorders(Page page, float x, float y, float cell_w, float cell_h) throws Exception {
        if (getBorder(Border.TOP) &&
            getBorder(Border.BOTTOM) &&
            getBorder(Border.LEFT) &&
            getBorder(Border.RIGHT)
        ) {
            page.drawRect(x, y, cell_w, cell_h);
        } else {
            if (getBorder(Border.TOP)) {
                page.moveTo(x, y);
                page.lineTo(x + cell_w, y);
                page.strokePath();
            }
            if (getBorder(Border.BOTTOM)) {
                page.moveTo(x, y + cell_h);
                page.lineTo(x + cell_w, y + cell_h);
                page.strokePath();
            }
            if (getBorder(Border.LEFT)) {
                page.moveTo(x, y);
                page.lineTo(x, y + cell_h);
                page.strokePath();
            }
            if (getBorder(Border.RIGHT)) {
                page.moveTo(x + cell_w, y);
                page.lineTo(x + cell_w, y + cell_h);
                page.strokePath();
            }
        }
    }
}
