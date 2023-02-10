package com.faltenreich.diaguard.feature.export.job.pdf.print;

import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportCache;
import com.pdfjet.Letter;
import com.pdfjet.Page;
import com.pdfjet.Point;

public class PdfPage extends Page {

    private static final float PADDING_EDGES = 60;
    static final float MARGIN = 20;

    private final Point position;
    private PdfFooter footer;

    public PdfPage(PdfExportCache cache) throws Exception {
        super(cache.getPdf(), Letter.PORTRAIT);
        position = getStartPoint();

        if (cache.getConfig().exportHeader()) {
            PdfHeader header = new PdfHeader(cache);
            header.drawOn(this, position);
            position.setY(position.getY() + header.getHeight());
        }

        if (cache.getConfig().exportFooter()) {
            // We jump to the end of the page
            footer = new PdfFooter(cache);
            footer.drawOn(this, new Point(PADDING_EDGES, super.getHeight() - PADDING_EDGES));
        }
    }

    private Point getStartPoint() {
        return new Point(PADDING_EDGES, PADDING_EDGES);
    }

    public Point getEndPoint() {
        float footerOffset = footer != null ? footer.getHeight() + MARGIN : 0;
        float endX = super.getWidth() - PADDING_EDGES;
        float endY = super.getHeight() - PADDING_EDGES - footerOffset;
        return new Point(endX, endY);
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
}
