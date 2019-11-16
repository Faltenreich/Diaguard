package com.faltenreich.diaguard.export.pdf.print;

import android.util.Log;

import com.faltenreich.diaguard.export.pdf.meta.PdfExportCache;
import com.pdfjet.Letter;
import com.pdfjet.Page;
import com.pdfjet.Point;

public class PdfPage extends Page {

    private static final String TAG = PdfPage.class.getSimpleName();

    private static final float PADDING = 60;
    public static final float MARGIN = 20;

    private Point position;
    private PdfFooter footer;

    public PdfPage(PdfExportCache cache) throws Exception {
        super(cache.getPdf(), Letter.PORTRAIT);
        position = getStartPoint();

        if (cache.getConfig().isExportFooter()) {
            footer = new PdfFooter(cache);
            footer.drawOn(this, new Point(PADDING, super.getHeight() - PADDING));
        }
    }

    private Point getStartPoint() {
        return new Point(PADDING, PADDING);
    }

    public Point getEndPoint() {
        float footerOffset = footer != null ? footer.getHeight() + MARGIN : 0;
        return new Point(super.getWidth() - PADDING, super.getHeight() - PADDING - footerOffset);
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
