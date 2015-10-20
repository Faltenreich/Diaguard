package com.faltenreich.diaguard.util.export;

import com.pdfjet.Letter;
import com.pdfjet.PDF;
import com.pdfjet.Page;
import com.pdfjet.Point;

/**
 * Created by Faltenreich on 20.10.2015.
 */
public class PdfPage extends Page {

    private static final int PADDING_TOP = 60;
    private static final int PADDING_BOTTOM = 100;
    private static final int PADDING_HORIZONTAL = 50;

    public PdfPage(PDF pdf) throws Exception {
        super(pdf, Letter.PORTRAIT);
    }

    public Point getStartPoint() {
        return new Point(PADDING_HORIZONTAL, PADDING_TOP);
    }

    @Override
    public float getWidth() {
        return super.getWidth() - (PADDING_HORIZONTAL * 2);
    }

    @Override
    public float getHeight() {
        return super.getHeight() - PADDING_TOP - PADDING_BOTTOM;
    }
}
