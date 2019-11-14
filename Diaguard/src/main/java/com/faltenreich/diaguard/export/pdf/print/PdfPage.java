package com.faltenreich.diaguard.export.pdf.print;

import android.util.Log;

import com.faltenreich.diaguard.export.pdf.meta.PdfExportCache;
import com.pdfjet.Letter;
import com.pdfjet.Page;
import com.pdfjet.Point;

public class PdfPage extends Page {

    private static final String TAG = PdfPage.class.getSimpleName();

    private static final int PADDING_TOP = 80;
    private static final int PADDING_BOTTOM = 100;
    private static final int PADDING_HORIZONTAL = 60;

    private Point position;

    public PdfPage(PdfExportCache cache) throws Exception {
        super(cache.getPdf(), Letter.PORTRAIT);
        this.position = getStartPoint();
        new PdfFooter(cache).drawOn(this, position);
    }

    private Point getStartPoint() {
        return new Point(PADDING_HORIZONTAL, PADDING_TOP);
    }

    public Point getEndPoint() {
        return new Point(super.getWidth() - PADDING_HORIZONTAL, super.getHeight() - PADDING_BOTTOM);
    }

    @Override
    public float getWidth() {
        return getEndPoint().getX() - getStartPoint().getX();
    }

    @Override
    public float getHeight() {
        return getEndPoint().getY() - getStartPoint().getY();
    }

    public Point getPosition() {
        return position;
    }

    public boolean hasContent() {
        return getPosition().getY() != getStartPoint().getY();
    }

    public void draw(PdfPrintable printable) {
        try {
            printable.drawOn(this, position);
            position.setY(position.getY() + printable.getHeight());
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }
}
