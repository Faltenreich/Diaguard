package com.faltenreich.diaguard.export.pdf;

import android.util.Log;

import com.faltenreich.diaguard.export.pdf.print.Pdf;
import com.faltenreich.diaguard.export.pdf.print.PdfPage;
import com.pdfjet.CoreFont;
import com.pdfjet.Font;
import com.pdfjet.Point;

import java.io.File;

public class PdfExportCache {

    private static final String TAG = PdfExportCache.class.getSimpleName();

    private PdfExportConfig config;
    private Font fontNormal;
    private Font fontBold;

    private Pdf pdf;
    private PdfPage page;
    private Point currentPosition;

    public PdfExportCache(PdfExportConfig config, File file) throws Exception {
        this.config = config;
        this.pdf = new Pdf(file, config);
        this.page = new PdfPage(pdf);
        this.fontNormal = new Font(pdf, CoreFont.HELVETICA);
        this.fontBold = new Font(pdf, CoreFont.HELVETICA_BOLD);
        this.currentPosition = new Point();
    }

    public PdfExportConfig getConfig() {
        return config;
    }

    public Font getFontNormal() {
        return fontNormal;
    }

    public Font getFontBold() {
        return fontBold;
    }

    public Pdf getPdf() {
        return pdf;
    }

    public PdfPage getPage() {
        return page;
    }

    public void newPage() {
        try {
            this.page = new PdfPage(pdf);
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Point currentPosition) {
        this.currentPosition = currentPosition;
    }
}
