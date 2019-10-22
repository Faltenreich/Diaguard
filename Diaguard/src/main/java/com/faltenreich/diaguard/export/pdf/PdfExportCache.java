package com.faltenreich.diaguard.export.pdf;

import android.util.Log;

import com.pdfjet.CoreFont;
import com.pdfjet.Font;
import com.pdfjet.Point;

import java.io.File;

public class PdfExportCache {

    private static final String TAG = PdfExportCache.class.getSimpleName();

    private PdfExportConfig config;
    private Pdf pdf;
    private PdfPage page;
    private Font fontNormal;
    private Font fontBold;
    private Point currentPosition;

    PdfExportCache(PdfExportConfig config, File file) throws Exception {
        this.config = config;
        this.pdf = new Pdf(file, config);
        this.page = new PdfPage(pdf);
        this.fontNormal = new Font(pdf, CoreFont.HELVETICA);
        this.fontBold = new Font(pdf, CoreFont.HELVETICA_BOLD);
        this.currentPosition = new Point();
    }

    PdfExportConfig getConfig() {
        return config;
    }

    Pdf getPdf() {
        return pdf;
    }

    public PdfPage getPage() {
        return page;
    }

    void newPage() {
        try {
            this.page = new PdfPage(pdf);
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

    Font getFontNormal() {
        return fontNormal;
    }

    Font getFontBold() {
        return fontBold;
    }

    Point getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Point currentPosition) {
        this.currentPosition = currentPosition;
    }
}
